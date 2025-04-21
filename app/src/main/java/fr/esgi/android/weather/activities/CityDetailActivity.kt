package fr.esgi.android.weather.activities

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.core.view.size
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherType
import fr.esgi.android.weather.api.WeatherAPI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class CityDetailActivity : WeatherActivity(R.layout.activity_city_detail, R.id.home) {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    private lateinit var background: ImageView
    private lateinit var city: TextView
    private lateinit var temperature: TextView
    private lateinit var forecast: LinearLayout
    private lateinit var air: TextView

    private var selectedCityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        background = findViewById<ImageView>(R.id.background_image)

        city = findViewById<TextView>(R.id.cityText)
        city.text = getString(R.string.loading)
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

        selectedCityName = intent.getStringExtra("CITY_NAME")

        fetchWeather()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchWeather() {
        if (selectedCityName == null) {
            Log.e("CityDetailActivity", "City name not provided")
            return
        }

        val city = WeatherAPI.searchCity(selectedCityName!!).get().first()
        val weather = WeatherAPI.getCurrentWeather(city).get()

        this.city.text = city.name + ", " + city.country
        temperature.text = "${weather.temperature}°C"

        background.setImageDrawable(getWeatherDrawable(weather.weather, !weather.isDay))

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

        val aqi = 20
        val note = (100 - aqi) / 20
        air.text = air.text.toString().replace("{note}", "$note")
    }

    private fun getWeatherDrawable(weather: WeatherType, night: Boolean): Drawable? {
        val id = weather.getResourceID(night)
        return ResourcesCompat.getDrawable(resources, id, null)
    }
}
