package fr.esgi.android.weather.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import fr.esgi.android.weather.R
import androidx.core.content.edit

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var locationSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var nightModeSwitch: Switch
    private lateinit var settingsTitle: TextView
    private lateinit var applyButton: Button

    private lateinit var sharedPreferences: SharedPreferences
    private val locationKey = "location_enabled"
    private val nightModeKey = "night_mode_enabled"

    private var locationEnabled = false
    private var nightModeEnabled = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationSwitch = findViewById(R.id.locationSwitch)
        nightModeSwitch = findViewById(R.id.nightModeSwitch)
        settingsTitle = findViewById(R.id.settingsTitle)
        applyButton = findViewById(R.id.applyButton)

        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        loadPreferences()

        locationSwitch.setOnCheckedChangeListener(null)
        nightModeSwitch.setOnCheckedChangeListener(null)

        locationSwitch.isChecked = locationEnabled
        nightModeSwitch.isChecked = nightModeEnabled

        updateNightModeSwitchText(nightModeEnabled)

        applyButton.setOnClickListener {
            locationEnabled = locationSwitch.isChecked
            nightModeEnabled = nightModeSwitch.isChecked
            savePreferences()

            if (nightModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            updateNightModeSwitchText(nightModeEnabled)
        }

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            locationEnabled = isChecked
        }

        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            nightModeEnabled = isChecked
            updateNightModeSwitchText(isChecked)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadPreferences() {
        locationEnabled = sharedPreferences.getBoolean(locationKey, false)
        nightModeEnabled = sharedPreferences.getBoolean(nightModeKey, false)

        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun savePreferences() {
        sharedPreferences.edit {
            putBoolean(locationKey, locationEnabled)
            putBoolean(nightModeKey, nightModeEnabled)
        }
    }

    private fun updateNightModeSwitchText(isNightModeEnabled: Boolean) {
        if (isNightModeEnabled) {
            nightModeSwitch.text = "Repasser en mode clair"
        } else {
            nightModeSwitch.text = "Activer le mode sombre"
        }
    }
}
