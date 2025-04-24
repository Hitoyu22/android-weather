package fr.esgi.android.weather.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherApp

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    private lateinit var darkMode: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.apply_settings).setOnClickListener {
            savePreferences()
        }

        darkMode = findViewById(R.id.dark_mode_switch)
        darkMode.isChecked = (application as WeatherApp).isDarkMode()
    }

    private fun savePreferences() {
        (application as WeatherApp).setDarkMode(darkMode.isChecked)

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode.isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}
