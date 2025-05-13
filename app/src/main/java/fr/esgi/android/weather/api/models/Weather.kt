package fr.esgi.android.weather.api.models

import android.content.Context
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherType
import java.time.LocalDate

data class Weather(
    val date: LocalDate,
    val temperature: Double?,
    val isDay: Boolean,
    val weather: WeatherType
){
    fun toString(context: Context): String {
        return when(weather) {
            WeatherType.SUNNY -> context.getString(R.string.weather_type_sunny)
            WeatherType.CLOUDY -> context.getString(R.string.weather_type_cloud)
            WeatherType.RAINY -> context.getString(R.string.weather_type_rainy)
            WeatherType.SNOW -> context.getString(R.string.weather_type_snow)
            WeatherType.THUNDER -> context.getString(R.string.weather_type_thunder)
            else -> context.getString(R.string.weather_type_unknown)
        }
    }
}
