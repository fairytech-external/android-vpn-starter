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

import ai.fairytech.moment.poc.MainActivity
import ai.fairytech.moment.poc.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * 알림을 보내기 위한 모듈
 */
class NotificationController private constructor() {
    private lateinit var context: Context

    companion object {
        const val NOTIFICATION_CHANNEL_ID: String = "notification_channel_id"
        const val NOTIFICATION_ID = 1234
        const val SERVICE_NOTIFICATION_CHANNEL_ID: String = "service_notification_channel_id"
        const val SERVICE_NOTIFICATION_ID = 1235

        private var _instance: NotificationController? = null
        fun getInstance(): NotificationController? {
            if (_instance == null) {
                synchronized(NotificationController::class.java) {
                    if (_instance == null) {
                        _instance = NotificationController()
                    }
                }
            }
            return _instance
        }
    }

    fun init(context: Context) {
        this.context = context
        createNotificationChannel()
        createServiceNotificationChannel()
    }

    fun sendNotification(title: String, description: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        val builder = context.let {
            NotificationCompat.Builder(it, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1) Notification channel for transaction match.
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "비즈니스 인식 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Register the channels with the system.
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            with(notificationManager) {
                createNotificationChannel(channel)
            }
        }
    }

    private fun createServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1) Notification channel for transaction match.
            val channel = NotificationChannel(
                SERVICE_NOTIFICATION_CHANNEL_ID,
                "비즈니스 인식 서비스",
                NotificationManager.IMPORTANCE_MIN
            )
            // Register the channels with the system.
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            with(notificationManager) {
                createNotificationChannel(channel)
            }
        }
    }
}