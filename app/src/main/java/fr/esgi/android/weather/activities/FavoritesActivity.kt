package fr.esgi.android.weather.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import fr.esgi.android.weather.lists.FavoritesAdapter

class FavoritesActivity : WeatherActivity(R.layout.activity_favorites, R.id.favorites) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favorites = HashMap<City, Weather>()
        getCity(favorites, "Gagny")
        getCity(favorites, "Lyon")
        getCity(favorites, "Paris")
        getCity(favorites, "Nation")
        getCity(favorites, "Neuilly-Plaisance")
        getCity(favorites, "Londres")
        getCity(favorites, "New York City")
        getCity(favorites, "Pékin")
        getCity(favorites, "Tokyo")
        getCity(favorites, "Berlin")
        getCity(favorites, "Washington")
        getCity(favorites, "Nowhere")
        getCity(favorites, "Malaga")
        getCity(favorites, "Beirut")
        getCity(favorites, "Villemomble")

        val list = findViewById<RecyclerView>(R.id.favorites_list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = FavoritesAdapter(favorites)
    }

    private fun getCity(favorites: HashMap<City, Weather>, city: String) {
        val city = WeatherAPI.searchCity(city).get().first()
        val weather = WeatherAPI.getCurrentWeather(city).get()
        favorites.put(city, weather)
    }

}