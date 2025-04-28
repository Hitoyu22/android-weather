package fr.esgi.android.weather.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherApp
import java.util.Locale

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    private lateinit var themeSetting: TextView
    private lateinit var languageSetting: TextView

    private val languageFrench = "Français"
    private val languageEnglish = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeSetting = findViewById(R.id.theme_setting)
        languageSetting = findViewById(R.id.language_setting)

        themeSetting.setOnClickListener {
            showThemeDialog()
        }

        languageSetting.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showThemeDialog() {
        val items = arrayOf(
            getString(R.string.light_mode),
            getString(R.string.dark_mode),
            getString(R.string.system_default)
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.select_theme)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            .show()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf(languageFrench, languageEnglish)

        AlertDialog.Builder(this)
            .setTitle(R.string.select_language)
            .setItems(languages) { _, which ->
                when (which) {
                    0 -> setLanguage("fr")
                    1 -> setLanguage("en")
                }
            }
            .show()
    }

    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        recreate()
    }
}
