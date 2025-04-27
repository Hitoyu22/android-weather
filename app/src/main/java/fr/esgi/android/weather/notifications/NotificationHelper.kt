package fr.esgi.android.weather.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import fr.esgi.android.weather.R

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "weather_alerts"
        private var sent = false
    }

    init {
        val channel = NotificationChannel(CHANNEL_ID, "Weather Alerts", NotificationManager.IMPORTANCE_HIGH)
        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                )
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendNotification(title: String, message: String) {
        if (sent) return
        sent = true
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
