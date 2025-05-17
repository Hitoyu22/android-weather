package fr.esgi.android.weather

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import fr.esgi.android.weather.activities.HomeActivity
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.WeatherSource
import fr.esgi.android.weather.api.models.City

class WeatherApp : Application() {

    companion object {
        const val APP_PREFERENCES_KEY = "app_preferences"
        const val DARK_MODE_KEY = "night_mode_enabled"
        const val FAVORITE_CITIES_KEY = "favorite_cities"
        const val WEATHER_SOURCE_KEY = "weather_source_cities"

        fun getPendingIntent(context: Context): PendingIntent? = PendingIntent.getActivity(
            context,
            0,
            Intent(context, HomeActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private lateinit var preferences: SharedPreferences
    private var darkMode: Boolean? = null
    private val favorites = mutableListOf<City>()

    override fun onCreate() {
        super.onCreate()

        preferences = getSharedPreferences(APP_PREFERENCES_KEY, MODE_PRIVATE)

        darkMode = if (preferences.contains(DARK_MODE_KEY))
            preferences.getBoolean(DARK_MODE_KEY, false)
        else null
        setDarkMode(darkMode)

        preferences.getStringSet(FAVORITE_CITIES_KEY, emptySet())?.forEach {
            val coordinates = it.split(",")
            val lat = coordinates[0].toDoubleOrNull()
            val lon = coordinates[1].toDoubleOrNull()

            if (lat != null && lon != null) {
                WeatherAPI.getCityFromCoordinates(lat, lon).thenAcceptAsync { favorites.add(it) }
            }
        }

        WeatherAPI.source = WeatherSource.get(preferences.getString(WEATHER_SOURCE_KEY, "meteofrance_seamless"))
    }

    fun setDarkMode(boolean: Boolean?) {
        AppCompatDelegate.setDefaultNightMode(
            when (boolean) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
        darkMode = boolean
        preferences.edit(true) {
            if (boolean == null) remove(DARK_MODE_KEY)
            else putBoolean(DARK_MODE_KEY, boolean)
        }
    }

    fun setFavorites(favorites: List<City>) {
        preferences.edit(true) {
            putStringSet(FAVORITE_CITIES_KEY, favorites.map { "${it.latitude},${it.longitude}" }.toSet())
        }

        this.favorites.clear()
        this.favorites.addAll(favorites)
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
