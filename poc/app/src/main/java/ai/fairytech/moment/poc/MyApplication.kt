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

package ai.fairytech.moment.poc

import ai.fairytech.moment.MomentSDK
import ai.fairytech.moment.exception.MomentException
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast

class MyApplication: Application() {
    val moment: MomentSDK by lazy {
        MomentSDK.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        createServiceNotificationChannel()
        createNotificationChannel()
        moment.restartIfNeeded(getConfig(applicationContext), object :
            MomentSDK.RestartResultCallback {
            override fun onSuccess(resultCode: MomentSDK.RestartResultCode) {
                if (resultCode == MomentSDK.RestartResultCode.SERVICE_RESTARTED) {
                    Toast.makeText(
                        applicationContext,
                        "restart success: $resultCode",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(exception: MomentException) {
                Toast.makeText(applicationContext, "restart failure.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getConfig(context: Context): MomentSDK.Config {
        return MomentSDK.Config(context)
            .notificationChannelId(NotificationController.NOTIFICATION_CHANNEL_ID)                // Notification channel id
            .notificationId(NotificationController.NOTIFICATION_ID)                               // Notification id
            .notificationIconResId(R.drawable.baseline_mood_24)
            .serviceNotificationChannelId(NotificationController.SERVICE_NOTIFICATION_CHANNEL_ID) // Service notification channel id
            .serviceNotificationId(NotificationController.SERVICE_NOTIFICATION_ID)
            .serviceNotificationIconResId(R.drawable.baseline_mood_24)
            .serviceNotificationTitle("Business recognition service")
            .serviceNotificationText("Service is running")
    }

    private fun createServiceNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_ID,
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            channel.description = NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_DESCRIPTION
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.NOTIFICATION_CHANNEL_ID,
                NotificationConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.description = NotificationConstants.NOTIFICATION_CHANNEL_DESCRIPTION
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

