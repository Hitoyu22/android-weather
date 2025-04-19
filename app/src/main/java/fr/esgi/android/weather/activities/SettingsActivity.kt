package fr.esgi.android.weather.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import fr.esgi.android.weather.R
import androidx.core.content.edit

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    companion object {
        const val DARK_MODE_KEY = "night_mode_enabled"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var darkMode: SwitchCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.apply_settings).setOnClickListener {
            savePreferences()
        }

        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        darkMode = findViewById(R.id.dark_mode_switch)
        darkMode.isChecked = sharedPreferences.getBoolean(DARK_MODE_KEY, false)
    }

    private fun savePreferences() {
        sharedPreferences.edit(true) {
            putBoolean(DARK_MODE_KEY, darkMode.isChecked)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode.isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}
