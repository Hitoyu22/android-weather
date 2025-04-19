package fr.esgi.android.weather.api.models

import fr.esgi.android.weather.WeatherType
import java.time.LocalDate

data class Weather(
    val date: LocalDate,
    val temperature: Double?,
    val isDay: Boolean,
    val weather: WeatherType
)
