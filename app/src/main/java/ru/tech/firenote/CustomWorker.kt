package ru.tech.firenote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class CustomWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        makeStatusNotification("done", appContext)

        return Result.success()
    }

    companion object {
        fun makeStatusNotification(message: String, context: Context) {

            // Make a channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                val name = "VERBOSE_NOTIFICATION_CHANNEL_NAME"
                val description = "VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(2.toString(), name, importance)
                channel.description = description

                // Add the channel
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

            // Create the notification
            val builder = NotificationCompat.Builder(context, 2.toString())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("NOTIFICATION_TITLE")
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(LongArray(0))

            // Show the notification
            NotificationManagerCompat.from(context).notify(2, builder.build())
        }
    }


}