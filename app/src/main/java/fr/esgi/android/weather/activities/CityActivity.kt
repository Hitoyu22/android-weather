package fr.esgi.android.weather.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

abstract class CityActivity(layout0: Int) : WeatherActivity(layout0, R.id.home) {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    protected lateinit var cityView: TextView
    protected lateinit var temperature: TextView
    protected lateinit var forecast: LinearLayout
    protected lateinit var air: TextView

    protected lateinit var city: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityView = findViewById<TextView>(R.id.cityText)
        cityView.text = getString(R.string.loading)

        temperature = findViewById<TextView>(R.id.currentTemp)
        temperature.text = "...°C"

        findViewById<TextView>(R.id.dateText).text = LocalDate.now().format(dateFormatter)

        forecast = findViewById<LinearLayout>(R.id.forecastList)
        (0 until 7).forEach { i ->
            val view = layoutInflater.inflate(R.layout.weather_forecast_item, forecast, false)
            forecast.addView(view)
        }

        air = findViewById<TextView>(R.id.airQualityText)
        air.text = getString(R.string.air_quality)
    }

    protected fun getCity(latitude: Double, longitude: Double): City {
        if (latitude == -1.0) return WeatherAPI.searchCities("Paris").get().first()
        return WeatherAPI.getCityFromCoordinates(latitude, longitude).get()
    }

    @SuppressLint("SetTextI18n")
    protected open fun fetchWeather(): Weather {
        val weather = WeatherAPI.getCurrentWeather(city).get()

        cityView.text = "${city.name}, ${city.country}"
        temperature.text = "${weather.temperature}°C"

        val today = LocalDate.now()
        val day = today.dayOfWeek.value
        val start = today.minusDays(day - 1L)
        val end = today.plusDays(7L - day)
        val week = WeatherAPI.getWeather(city, start.toString(), end.toString()).get()

        forecast.removeAllViews()

        for (day in week) {
            if (day.temperature != null) {
                val view = layoutInflater.inflate(R.layout.weather_forecast_item, forecast, false)
                view.findViewById<TextView>(R.id.day).text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + "."
                view.findViewById<TextView>(R.id.icon).text = day.weather.icon
                view.findViewById<TextView>(R.id.temperature).text = "${day.temperature}°C"
                forecast.addView(view)
            }
        }

        val aqi = WeatherAPI.getAirQuality(city).get()
        val note = (100 - aqi) / 20.0
        air.text = air.text.toString().replace("{note}", "$note")

        return weather
    }

}