package fr.esgi.android.weather.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import fr.esgi.android.weather.lists.FavoritesAdapter

class FavoritesActivity : WeatherActivity(R.layout.activity_favorites, R.id.favorites) {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)

        val favorites = HashMap<City, Weather>()
        val favoriteCities = getFavoriteCities()

        for (cityName in favoriteCities) {
            getCity(favorites, cityName)
        }

        val list = findViewById<RecyclerView>(R.id.favorites_list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = FavoritesAdapter(favorites)
    }

    private fun getCity(favorites: HashMap<City, Weather>, cityName: String) {
        val city = WeatherAPI.searchCity(cityName).get().first()
        val weather = WeatherAPI.getCurrentWeather(city).get()
        favorites[city] = weather
    }

    private fun getFavoriteCities(): Set<String> {
        return sharedPreferences.getStringSet("FavoriteCities", emptySet()) ?: emptySet()
    }
}
