package fr.esgi.android.weather.activities

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import fr.esgi.android.weather.R
import fr.esgi.android.weather.network.WeatherAPI
import fr.esgi.android.weather.notifications.NotificationHelper

class HomeActivity : WeatherActivity(R.layout.activity_main, R.id.home) {

    private lateinit var notificationHelper: NotificationHelper
    private val handler = Handler(Looper.getMainLooper())
    private val notificationRunnable = object : Runnable {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun run() {
            Log.d("Notification", "Sending Notification")
            val weather = WeatherAPI.getWeather("Paris", null).get()
            notificationHelper.sendNotification("Weather Alert", "It's time for a weather update! " + weather.weather.joinToString(", ") { it.main })
            Log.d("Notification", "Notification sent")
            handler.postDelayed(this, 60000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationHelper = NotificationHelper(this)

        handler.post(notificationRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(notificationRunnable)
    }
}