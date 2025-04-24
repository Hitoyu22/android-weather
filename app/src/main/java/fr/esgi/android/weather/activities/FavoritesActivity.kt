package fr.esgi.android.weather.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherApp
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import fr.esgi.android.weather.lists.FavoritesAdapter

class FavoritesActivity : WeatherActivity(R.layout.activity_favorites, R.id.favorites) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favorites = HashMap<City, Weather>()

        (application as WeatherApp).getFavorites().forEach { city ->
            val weather = WeatherAPI.getCurrentWeather(city).get()
            favorites[city] = weather
        }

        val list = findViewById<RecyclerView>(R.id.favorites_list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = FavoritesAdapter(favorites)
    }

}
