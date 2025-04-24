package fr.esgi.android.weather.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherType
import fr.esgi.android.weather.api.models.Weather
import fr.esgi.android.weather.notifications.NotificationHelper
import fr.esgi.android.weather.location.LocationHelper

class HomeActivity : CityActivity(R.layout.activity_main) {

    private lateinit var notificationHelper: NotificationHelper
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val notificationRunnable = object : Runnable {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun run() {
            Log.d("Notification", "Sending Notification")
            notificationHelper.sendNotification(
                "Weather Alert",
                "" // "It's time for a weather update! " + weather.weather.joinToString(", ") { it.main }
            )
            Log.d("Notification", "Notification sent")
            handler.postDelayed(this, 60000)
        }
    }

    private lateinit var background: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        background = findViewById<ImageView>(R.id.background_image)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1000
            )
        } else fetchCity()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fetchCity()
    }

    private fun fetchCity() {
        LocationHelper.getLastKnownLocation(this, fusedLocationClient,
            object : LocationHelper.LocationCallbackListener {
                override fun onLocationResult(location: Location) {
                    city = getCity(location.latitude, location.longitude)
                    fetchWeather()
                }
                override fun onLocationUnavailable() {
                    city = getCity(-1.0, -1.0)
                    fetchWeather()
                }
            }
        )
    }

    override fun fetchWeather(): Weather {
        val weather = super.fetchWeather()
        background.setImageDrawable(getWeatherDrawable(weather.weather, !weather.isDay))
        return weather
    }

    private fun getWeatherDrawable(weather: WeatherType, night: Boolean): Drawable? {
        val id = weather.getResourceID(night)
        return ResourcesCompat.getDrawable(resources, id, null)
    }
}
