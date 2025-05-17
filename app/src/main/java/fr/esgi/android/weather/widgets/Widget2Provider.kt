package fr.esgi.android.weather.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.helpers.LocationHelper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import androidx.core.content.ContextCompat
import fr.esgi.android.weather.WeatherType
import fr.esgi.android.weather.api.models.City
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class Widget2Provider : AppWidgetProvider() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget2_layout)
            val defaultLocation = "Paris, France"
            val defaultTemperature = "15°C"
            views.setTextViewText(R.id.location, defaultLocation)
            views.setTextViewText(R.id.temperature, defaultTemperature)

            LocationHelper.getLastKnownLocation(context, fusedLocationClient,
                object : LocationHelper.LocationCallbackListener {
                    override fun onLocationResult(location: Location) = fetchWeatherAndUpdateWidget(context, views, appWidgetManager, appWidgetId, location.latitude, location.longitude)
                    override fun onLocationUnavailable() = fetchWeatherAndUpdateWidget(context, views, appWidgetManager, appWidgetId, 48.8566, 2.3522)
                }
            )
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
        val city = WeatherAPI.getCityFromCoordinates(latitude, longitude).get()
        val weather = WeatherAPI.getCurrentWeather(city).get()

        views.setTextViewText(R.id.location, city.name)
        views.setTextViewText(R.id.temperature, "${weather.temperature}°C")

        val weatherIconDrawable = getWeatherIconDrawable(context, weather.weather)
        views.setImageViewBitmap(R.id.weather_icon, weatherIconDrawable)

        fetchForecastAndUpdateWidget(context, views, city)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun fetchForecastAndUpdateWidget(
        context: Context,
        views: RemoteViews,
        city: City
    ) {
        val today = LocalDate.now()
        val day = today.dayOfWeek.value
        val start = today.minusDays(day - 1L)
        val end = today.plusDays(7L - day)
        val weekForecast = WeatherAPI.getWeather(city, start.toString(), end.toString()).get()

        views.removeAllViews(R.id.forecastList)

        for (dayForecast in weekForecast) {
            if (dayForecast.temperature != null) {
                val itemView = RemoteViews(context.packageName, R.layout.widget_forecast_item)
                itemView.setTextViewText(R.id.day, dayForecast.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                itemView.setTextViewText(R.id.icon, dayForecast.weather.iconText)
                itemView.setTextViewText(R.id.temperature, "${dayForecast.temperature}°C")

                views.addView(R.id.forecastList, itemView)
            }
        }
    }

    private fun getWeatherIconDrawable(context: Context, weather: WeatherType): Bitmap {
        val weatherDrawableId = weather.getWidgetIcon()
        val drawable = ContextCompat.getDrawable(context, weatherDrawableId)
        return (drawable as BitmapDrawable).bitmap
    }
}
