package fr.esgi.android.weather.widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.location.LocationHelper
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import androidx.core.content.ContextCompat
import fr.esgi.android.weather.WeatherType

class Widget1Provider : AppWidgetProvider() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("RemoteViewLayout")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget1_layout)
            val defaultLocation = "Paris, France"
            val defaultTemperature = "15°C"
            views.setTextViewText(R.id.location, defaultLocation)
            views.setTextViewText(R.id.temperature, defaultTemperature)

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationHelper.getLastKnownLocation(
                    context,
                    fusedLocationClient,
                    object : LocationHelper.LocationCallbackListener {
                        override fun onLocationResult(location: Location) {
                            fetchWeatherAndUpdateWidget(context, views, appWidgetManager, appWidgetId, location.latitude, location.longitude)
                        }

                        override fun onLocationUnavailable() {
                            fetchWeatherAndUpdateWidget(context, views, appWidgetManager, appWidgetId, 48.8566, 2.3522)
                        }
                    }
                )
            } else {
                fetchWeatherAndUpdateWidget(context, views, appWidgetManager, appWidgetId, 48.8566, 2.3522)
            }
        }
    }

    private fun fetchWeatherAndUpdateWidget(
        context: Context,
        views: RemoteViews,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        latitude: Double,
        longitude: Double
    ) {
        val weather = WeatherAPI.getCurrentWeatherWithCoordinates(latitude, longitude).get()
        val locationInfo = WeatherAPI.getCityNameWithCoordinates(latitude, longitude).get()

        views.setTextViewText(R.id.location, locationInfo.city)
        views.setTextViewText(R.id.temperature, "${weather.temperature}°C")

        val backgroundDrawable = getWeatherDrawable(context, weather.weather, !weather.isDay)
        views.setImageViewBitmap(R.id.background_image, backgroundDrawable)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getWeatherDrawable(context: Context, weather: WeatherType, isNight: Boolean): Bitmap {
        val weatherDrawableId = weather.getResourceID(isNight)
        val drawable = ContextCompat.getDrawable(context, weatherDrawableId)
        return (drawable as BitmapDrawable).bitmap
    }
}
