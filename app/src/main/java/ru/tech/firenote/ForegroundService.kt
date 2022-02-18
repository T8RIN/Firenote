package ru.tech.firenote

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class ForegroundService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_fire_144)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        func?.invoke()

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                makeStatusNotification(
                    "${Calendar.getInstance()[Calendar.HOUR_OF_DAY]}:${Calendar.getInstance()[Calendar.MINUTE]}:${Calendar.getInstance()[Calendar.SECOND]}",
                    applicationContext
                )
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        var func: (() -> Unit)? = null
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}