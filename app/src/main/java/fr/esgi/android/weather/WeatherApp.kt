package fr.esgi.android.weather

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City

class WeatherApp : Application() {

    companion object {
        const val APP_PREFERENCES_KEY = "app_preferences"
        const val DARK_MODE_KEY = "night_mode_enabled"
        const val FAVORITE_CITIES_KEY = "favorite_cities"
    }

    private lateinit var preferences: SharedPreferences
    private var darkMode = false
    private val favorites = ArrayList<City>()

    override fun onCreate() {
        super.onCreate()

        preferences = getSharedPreferences(APP_PREFERENCES_KEY, MODE_PRIVATE)

        darkMode = preferences.getBoolean(DARK_MODE_KEY, false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        preferences.getStringSet(FAVORITE_CITIES_KEY, emptySet())?.forEach {
            val coordinates = it.split(",")
            val lat = coordinates[0].toDoubleOrNull()
            val lon = coordinates[1].toDoubleOrNull()

            if (lat == null || lon == null) return
            favorites.add(WeatherAPI.getCityFromCoordinates(lat, lon).get())
        }
    }

    fun setDarkMode(darkMode: Boolean) {
        preferences.edit(true) {
            putBoolean(DARK_MODE_KEY, darkMode)
        }
    }

    fun isDarkMode(): Boolean {
        return darkMode
    }

    fun getFavorites(): List<City> {
        return favorites
    }

    fun toggleFavorite(city: City) {
        if (!favorites.remove(city)) favorites.add(city)
        preferences.edit(true) {
            putStringSet(FAVORITE_CITIES_KEY, favorites.map { "${it.latitude},${it.longitude}" }.toSet())
        }
    }

    fun isFavorite(city: City): Boolean {
        return favorites.contains(city)
    }

}
