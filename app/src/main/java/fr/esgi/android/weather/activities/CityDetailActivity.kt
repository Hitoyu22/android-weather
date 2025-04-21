package fr.esgi.android.weather.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.edit
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherType
import fr.esgi.android.weather.api.WeatherAPI
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CityDetailActivity : WeatherActivity(R.layout.activity_city_detail, R.id.home) {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    private lateinit var mapView: MapView
    private lateinit var background: ImageView
    private lateinit var city: TextView
    private lateinit var temperature: TextView
    private lateinit var forecast: LinearLayout
    private lateinit var air: TextView
    private lateinit var favoriteButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var selectedCityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val osmConfig = Configuration.getInstance()
        osmConfig.load(applicationContext, getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        osmConfig.userAgentValue = packageName

        val basePath = File(filesDir, "osmdroid")
        if (!basePath.exists()) basePath.mkdirs()

        val tileCache = File(cacheDir, "osmdroid/tiles")
        if (!tileCache.exists()) tileCache.mkdirs()

        osmConfig.osmdroidBasePath = basePath
        osmConfig.osmdroidTileCache = tileCache

        mapView = findViewById(R.id.mapview)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        background = findViewById(R.id.background_image)
        city = findViewById(R.id.cityText)
        temperature = findViewById(R.id.currentTemp)
        findViewById<TextView>(R.id.dateText).text = LocalDate.now().format(dateFormatter)
        forecast = findViewById(R.id.forecastList)
        air = findViewById(R.id.airQualityText)
        favoriteButton = findViewById(R.id.favoriteButton)

        sharedPreferences = getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)

        favoriteButton.setOnClickListener {
            toggleFavoriteCity()
        }

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

        this.city.text = "${city.name}, ${city.country}"
        temperature.text = "${weather.temperature}°C"

        val geoPoint = GeoPoint(city.latitude, city.longitude)
        val mapController = mapView.controller
        mapController.setZoom(10.0)
        mapController.setCenter(geoPoint)

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

        mapView.post {
            val geoPoint = GeoPoint(city.latitude, city.longitude)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(geoPoint)
        }

        updateFavoriteButton()
    }

    private fun getWeatherDrawable(weather: WeatherType, night: Boolean): Drawable? {
        val id = weather.getResourceID(night)
        return ResourcesCompat.getDrawable(resources, id, null)
    }

    private fun toggleFavoriteCity() {
        val favorites = getFavoriteCities().toMutableSet()
        if (favorites.contains(selectedCityName)) {
            favorites.remove(selectedCityName)
            favoriteButton.text = "Ajouter aux favoris"
        } else {
            favorites.add(selectedCityName!!)
            favoriteButton.text = "Enlever des favoris"
        }
        saveFavoriteCities(favorites)
    }

    private fun updateFavoriteButton() {
        if (isCityFavorite(selectedCityName!!)) {
            favoriteButton.text = "Enlever des favoris"
        } else {
            favoriteButton.text = "Ajouter aux favoris"
        }
    }

    private fun getFavoriteCities(): Set<String> {
        return sharedPreferences.getStringSet("FavoriteCities", emptySet()) ?: emptySet()
    }

    private fun saveFavoriteCities(cities: Set<String>) {
        sharedPreferences.edit {
            putStringSet("FavoriteCities", cities)
        }
    }

    private fun isCityFavorite(cityName: String): Boolean {
        return getFavoriteCities().contains(cityName)
    }
}
