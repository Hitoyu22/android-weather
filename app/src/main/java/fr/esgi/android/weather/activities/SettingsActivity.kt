package fr.esgi.android.weather.activities

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import fr.esgi.android.weather.R
import java.util.Locale

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    private lateinit var themeSetting: TextView
    private lateinit var languageSetting: TextView
    private lateinit var aboutSetting: TextView

    private lateinit var languageFrench: String
    private lateinit var languageEnglish: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        languageFrench = getString(R.string.language_french)
        languageEnglish = getString(R.string.language_english)

        themeSetting = findViewById(R.id.theme_setting)
        languageSetting = findViewById(R.id.language_setting)
        aboutSetting = findViewById(R.id.about_setting)

        themeSetting.setOnClickListener {
            showThemeDialog()
        }

        languageSetting.setOnClickListener {
            showLanguageDialog()
        }

        aboutSetting.setOnClickListener {
            showAboutDialog()
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

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.about)
            .setMessage(R.string.about_message)
            .setPositiveButton(android.R.string.ok, null)
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
