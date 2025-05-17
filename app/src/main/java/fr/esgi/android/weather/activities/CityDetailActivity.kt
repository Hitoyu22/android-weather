package fr.esgi.android.weather.activities

import android.os.Bundle
import android.widget.*
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherApp
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.File

class CityDetailActivity : CityActivity(R.layout.activity_city_detail) {

    private lateinit var mapView: MapView
    private lateinit var favoriteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lat = intent.getDoubleExtra("CITY_LAT", -1.0)
        val lon = intent.getDoubleExtra("CITY_LON", -1.0)
        city = getCity(lat, lon)

        favoriteButton = findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            (application as WeatherApp).toggleFavorite(city)
            updateFavoriteButton()
        }
        updateFavoriteButton()


        val osmConfig = Configuration.getInstance()
        osmConfig.load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        osmConfig.userAgentValue = packageName
        osmConfig.osmdroidBasePath = loadCache("osmdroid")
        osmConfig.osmdroidTileCache = loadCache("osmdroid/tiles")

        mapView = findViewById(R.id.mapview)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val geoPoint = GeoPoint(city.latitude, city.longitude)
        val mapController = mapView.controller
        mapController.setZoom(10.0)
        mapController.setCenter(geoPoint)
        mapView.post {
            val geoPoint = GeoPoint(city.latitude, city.longitude)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(geoPoint)
        }

        fetchWeather()
    }

    private fun loadCache(file: String): File {
        val cache = File(cacheDir, file)
        if (!cache.exists()) cache.mkdirs()
        return cache
    }

    private fun updateFavoriteButton() {
        favoriteButton.text = getString(
            if ((application as WeatherApp).isFavorite(city)) R.string.remove_favorite
            else R.string.add_favorite
        )
    }
}
