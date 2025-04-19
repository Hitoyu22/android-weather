package fr.esgi.android.weather

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import fr.esgi.android.weather.activities.SettingsActivity

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val darkMode = getSharedPreferences("app_preferences", MODE_PRIVATE).getBoolean(SettingsActivity.DARK_MODE_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}
