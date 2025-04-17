package fr.esgi.android.weather.activities

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import fr.esgi.android.weather.R
import fr.esgi.android.weather.network.WeatherAPI
import fr.esgi.android.weather.notifications.NotificationHelper
import org.json.JSONObject

class HomeActivity : WeatherActivity(R.layout.activity_main, R.id.home) {

    private lateinit var notificationHelper: NotificationHelper
    private val handler = Handler(Looper.getMainLooper())

    private val notificationRunnable = object : Runnable {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun run() {
            Log.d("Notification", "Sending Notification")
            val weather = WeatherAPI.getWeather("Paris", null).get()
            notificationHelper.sendNotification(
                "Weather Alert",
                "It's time for a weather update! " + weather.weather.joinToString(", ") { it.main }
            )
            Log.d("Notification", "Notification sent")
            handler.postDelayed(this, 60000)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageDrawable(getWeatherDrawable())

        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isNightModeEnabled = sharedPreferences.getBoolean("night_mode_enabled", false)
        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val weatherData = loadWeatherData()

        findViewById<TextView>(R.id.cityText).text = weatherData.getString("city")
        findViewById<TextView>(R.id.currentTemp).text = "${weatherData.getInt("temperature")}°C"
        findViewById<TextView>(R.id.dateText).text = weatherData.getString("date")

        val airQuality = weatherData.getJSONObject("airQuality")
        findViewById<TextView>(R.id.airQualityText).text =
            "${airQuality.getString("description")}\nLa qualité de l’air est notée ${airQuality.getInt("score")}/5 aujourd’hui"

        val forecastLayout = findViewById<LinearLayout>(R.id.forecastList)
        val forecastArray = weatherData.getJSONArray("forecast")
        for (i in 0 until forecastArray.length()) {
            val dayData = forecastArray.getJSONObject(i)
            val view = layoutInflater.inflate(R.layout.weather_forecast_item, forecastLayout, false)
            view.findViewById<TextView>(R.id.dayText).text = dayData.getString("day")
            view.findViewById<TextView>(R.id.iconText).text = dayData.getString("icon")
            view.findViewById<TextView>(R.id.tempText).text = "${dayData.getInt("temp")}°C"
            forecastLayout.addView(view)
        }
    }

    private fun getWeatherDrawable(): Drawable? {
        val weatherType = "sunny"
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        return when (weatherType.lowercase()) {
            "sunny" -> loadImage(isDarkMode, "sunny")
            "cloudy" -> loadImage(isDarkMode, "cloudy")
            "rainy" -> loadImage(isDarkMode, "rainy")
            "snow" -> loadImage(isDarkMode, "snow")
            "thunder" -> loadImage(isDarkMode, "thunder")
            else -> loadImage(isDarkMode, "sunny")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DiscouragedApi")
    private fun loadImage(isDarkMode: Boolean, imageName: String): Drawable? {
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

    private fun loadWeatherData(): JSONObject {
        val jsonStr = """
            {
              "city": "Paris, France",
              "date": "7 juillet 2025",
              "temperature": 27,
              "forecast": [
                { "day": "Lun.", "icon": "☀️", "temp": 15 },
                { "day": "Mar.", "icon": "⛅", "temp": 15 },
                { "day": "Mer.", "icon": "⛅", "temp": 15 },
                { "day": "Jeu.", "icon": "🌧️", "temp": 15 },
                { "day": "Ven.", "icon": "⛈️", "temp": 15 }
              ],
              "airQuality": {
                "score": 5,
                "description": "Qualité de l’air excellente"
              }
            }
        """.trimIndent()
        return JSONObject(jsonStr)
    }
}
