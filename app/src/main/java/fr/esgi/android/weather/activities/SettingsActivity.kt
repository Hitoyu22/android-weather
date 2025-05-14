package fr.esgi.android.weather.activities

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import fr.esgi.android.weather.R
import fr.esgi.android.weather.WeatherApp
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.WeatherSource
import java.util.Locale

class SettingsActivity : WeatherActivity(R.layout.activity_settings, R.id.settings) {

    private lateinit var themeSetting: TextView
    private lateinit var languageSetting: TextView
    private lateinit var sourceSetting: TextView
    private lateinit var aboutSetting: TextView

    private lateinit var themes: Array<String>
    private lateinit var languages: Array<String>
    private lateinit var sources: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themes = arrayOf(R.string.light_mode, R.string.dark_mode, R.string.system_default).map { getString(it) }.toTypedArray()
        languages = arrayOf(R.string.language_french, R.string.language_english /* , R.string.system_default */).map { getString(it) }.toTypedArray()
        sources = WeatherSource.entries.map { getString(it.stringId) }.toTypedArray()

        themeSetting = findViewById(R.id.theme_setting)
        languageSetting = findViewById(R.id.language_setting)
        sourceSetting = findViewById(R.id.source_setting)
        aboutSetting = findViewById(R.id.about_setting)

        themeSetting.setOnClickListener { showThemeDialog() }
        languageSetting.setOnClickListener { showLanguageDialog() }
        sourceSetting.setOnClickListener { showSourceDialog() }
        aboutSetting.setOnClickListener { showAboutDialog() }
    }

    private fun showThemeDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.select_theme)
            .setItems(themes) { _, which ->
                (application as WeatherApp).setDarkMode(when (which) {
                    0 -> false
                    1 -> true
                    else -> null
                })
            }
            .show()
    }

    private fun showLanguageDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.select_language)
            .setItems(languages) { _, which ->
                setLanguage(when (which) {
                    0 -> "fr"
                    1 -> "en"
                    else -> null
                })
            }
            .show()
    }

    private fun showSourceDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.select_source)
            .setItems(sources) { _, which -> WeatherAPI.source = WeatherSource.entries[which] }
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.about)
            .setMessage(R.string.about_message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun setLanguage(languageCode: String?) {
        var locale: Locale? = null
        if (languageCode != null) {
            locale = Locale(languageCode)
            Locale.setDefault(locale)
        }

        val config = Configuration()
        config.setLocale(locale)

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        recreate()
    }
}
