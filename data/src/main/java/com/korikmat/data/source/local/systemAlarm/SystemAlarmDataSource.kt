package com.korikmat.data.source.local.systemAlarm

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class SystemAlarmDataSource (
    private val context: Context
) {

    private companion object {
        const val CHANNEL_ID = "timer_channel"
        const val WORK_TAG = "timer_notification_work"
    }

    fun schedule(delayMs: Long) {
        createChannel()

        val request = OneTimeWorkRequestBuilder<TimerWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_TAG,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel() {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
    }

    // ——— private helpers ———
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "alarm channel for timer notifications"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 300, 300)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    class TimerWorker(
        context: Context,
        params: WorkerParameters
    ) : CoroutineWorker(context, params) {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override suspend fun doWork(): Result {
            val n = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_popup_reminder)
                .setContentTitle("Watch Together")
                .setContentText("⏰ Time to watch a movie!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build()

            val uniqueId = System.currentTimeMillis().toInt()
            NotificationManagerCompat.from(applicationContext).notify(uniqueId, n)
            return Result.success()
        }
    }
}