package com.example.myapplication4.notifications

// NotificationWorker.kt
import android.content.Context
import androidx.work.*
import com.example.myapplication4.MainActivity
import java.util.concurrent.TimeUnit
import java.util.Calendar

class NotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val notificationHelper = NotificationHelper(context)

    override fun doWork(): Result {
        // Show the notification
        notificationHelper.showNotification(
            notificationId = 1,
            title = "Recordatorio Semanal",
            message = "No te olivides de ingresar tus gastos e ingresos!",
            targetActivity = MainActivity::class.java
        )
        return Result.success()
    }

    companion object {
        private const val WEEKLY_NOTIFICATION_WORK = "weekly_notification_work"

        fun schedule(context: Context) {
            // Calculate initial delay until next Sunday at 10:00 AM
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis

            // Set target time to 10:00 AM
            calendar.set(Calendar.HOUR_OF_DAY, 10)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            // If today is after 10:00 AM, move to next week
            if (calendar.timeInMillis <= now) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            // Find next Sunday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            val initialDelay = calendar.timeInMillis - now

            // Create the periodic WorkRequest
            val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                7, TimeUnit.DAYS) // Repeat every 7 days
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .build()

            // Schedule the work
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WEEKLY_NOTIFICATION_WORK,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    notificationWorkRequest
                )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(WEEKLY_NOTIFICATION_WORK)
        }
    }
}