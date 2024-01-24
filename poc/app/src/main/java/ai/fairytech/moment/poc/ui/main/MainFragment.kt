/*
 * Fairy Technologies CONFIDENTIAL
 * __________________
 *
 * Copyright (C) Fairy Technologies, Inc - All Rights Reserved
 *
 * NOTICE:  All information contained herein is, and remains the property of Fairy
 * Technologies Incorporated and its suppliers, if any. The intellectual and technical
 * concepts contained herein are proprietary to Fairy Technologies Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 *
 * Dissemination of this information,or reproduction or modification of this material
 * is strictly forbidden unless prior written permission is obtained from Fairy
 * Technologies Incorporated.
 *
 */

package ai.fairytech.moment.poc.ui.main

import ai.fairytech.moment.MomentSDK
import ai.fairytech.moment.exception.ErrorCode
import ai.fairytech.moment.exception.MomentException
import ai.fairytech.moment.poc.MyApplication
import ai.fairytech.moment.poc.NotificationConstants
import ai.fairytech.moment.poc.R
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ai.fairytech.moment.poc.databinding.FragmentMainBinding
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val moment: MomentSDK? by lazy {
        (activity?.application as MyApplication?)?.moment
    }

    // 알림 권한 허용
    private val notificationPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(context, "알림 권한 허용", Toast.LENGTH_SHORT).show()
            }
        }

    // 권한 허용
    private val appUsagePermissionLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (MomentSDK.isAppUsagePermissionGranted(requireContext().applicationContext)) {
                handleStart()
            } else {
                binding.startService.isEnabled = true
                binding.startService.isChecked = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        binding.startService.isChecked = moment?.isRunning == true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** 권한 관련 **/
        // 알림 권한 없을시 받음
        requireContext().let {
            if (!MomentSDK.isNotificationPermissionGranted(it)
                && canAskRuntimeNotiPermission()
            ) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }

            // 서비스를 시작하는 스위치
            binding.startService.isChecked = MomentSDK.isAppUsagePermissionGranted(it)
                    && moment?.isRunning == true
            binding.startService.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.startService.isEnabled = false
                    if (MomentSDK.isAppUsagePermissionGranted(it)) {
                        handleStart()
                    } else {
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        intent.data = Uri.fromParts("package", it.packageName, null)
                        appUsagePermissionLauncher.launch(intent)
                    }
                } else {
                    handleStop()
                }
            }
        }
    }

    // 서비스 시작
    private fun handleStart() {
        try {
            val config = MomentSDK.Config(requireContext())
                .serviceNotificationChannelId(NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_ID) // 서비스를 위해 필요한 채널아이디
                .serviceNotificationTitle("비즈니스 인식 서비스")
                .serviceNotificationText("비즈니스 인식 서비스 동작중")
                .serviceNotificationIconResId(R.drawable.baseline_mood_24)
                .serviceNotificationIconColorInt(resources.getColor(R.color.purple_500, null))
            moment?.start(config, object : MomentSDK.ResultCallback {
                override fun onSuccess() {
                    Handler(Looper.getMainLooper()).post {
                        binding.startService.isEnabled = true
                        binding.startService.isChecked = true
                    }
                }

                override fun onFailure(errorCode: ErrorCode, message: String) {
                    Handler(Looper.getMainLooper()).post {
                        binding.startService.isEnabled = true
                        binding.startService.isChecked = false
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        Log.e("poc", "$errorCode: $message")
                    }
                }
            })
        } catch (e: MomentException) {
            binding.startService.isEnabled = true
            binding.startService.isChecked = false
        }
    }

    // 서비스 정지
    private fun handleStop() {
        try {
            moment?.stop()
        } catch (e: MomentException) {
            binding.startService.isChecked = true
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Log.e("poc", "error: ${e.message}")
        }
    }

    private fun canAskRuntimeNotiPermission(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU;
    }
}