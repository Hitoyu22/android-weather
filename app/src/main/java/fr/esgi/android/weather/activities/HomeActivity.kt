package fr.esgi.android.weather.activities

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
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

        // notificationHelper = NotificationHelper(this)

        // handler.post(notificationRunnable)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageDrawable(getWeatherDrawable())
    }

    private fun getWeatherDrawable(): Drawable? {
        val weatherType = "cloudy"
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        return when (weatherType.lowercase()) {
            "sunny" -> loadImageFromDrawable(isDarkMode, "sunny")
            "cloudy" -> loadImageFromDrawable(isDarkMode, "cloudy")
            "rainy" -> loadImageFromDrawable(isDarkMode, "rainy")
            "snow" -> loadImageFromDrawable(isDarkMode, "snow")
            "thunder" -> loadImageFromDrawable(isDarkMode, "thunder")
            else -> loadImageFromDrawable(isDarkMode, "sunny")
        }
    }

    private fun loadImageFromDrawable(isDarkMode: Boolean, imageName: String): Drawable? {
        val imageNameWithSuffix = if (isDarkMode) {
            imageName + "_night"
        } else {
            imageName
        }

        val resourceId = resources.getIdentifier(imageNameWithSuffix, "drawable", packageName)

        if (resourceId == 0) {
            Log.e("HomeActivity", "Resource not found: $imageNameWithSuffix")
            return null
        }

        return resources.getDrawable(resourceId, null)
    }

    override fun onDestroy() {
        super.onDestroy()

        // handler.removeCallbacks(notificationRunnable)
    }
}
