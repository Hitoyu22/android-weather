package fr.esgi.android.weather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.lists.CitySearchAdapter
import kotlin.reflect.KClass

abstract class WeatherActivity(val layout: Int, val navId: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout)

        val navigation = findViewById<NavigationBarView>(R.id.navigation)
        navigation.selectedItemId = navId
        navigation.setOnItemSelectedListener { item ->
            Log.d("Navigation", "Item selected: ${item.title}")
            onNavBarClick(item)
        }

        if (findViewById<AutoCompleteTextView>(R.id.searchView) != null) {
            configureAutoCompleteTextView()
        }
    }

    private fun onNavBarClick(item: MenuItem): Boolean {
        val activity: KClass<out AppCompatActivity> = when (item.itemId) {
            R.id.favorites -> {
                Log.d("Navigation", "Favorites selected")
                FavoritesActivity::class
            }
            R.id.home -> {
                Log.d("Navigation", "Home selected")
                HomeActivity::class
            }
            R.id.settings -> {
                Log.d("Navigation", "Settings selected")
                SettingsActivity::class
            }
            else -> {
                Log.d("Navigation", "Unknown item selected")
                return false
            }
        }
        if (javaClass === activity.java) {
            Log.d("Navigation", "Already on the selected activity")
            return true
        }
        startActivity(Intent(this, activity.java))
        return true
    }

    protected fun configureAutoCompleteTextView() {
        val searchView: AutoCompleteTextView = findViewById(R.id.searchView)
        val adapter = CitySearchAdapter(this)
        searchView.setAdapter(adapter)

        searchView.setOnItemClickListener { parent, view, position, id ->
            val city = parent.adapter.getItem(position) as City

            Intent(this, CityDetailActivity::class.java).apply {
                putExtra("CITY_LAT", city.latitude)
                putExtra("CITY_LON", city.longitude)
                startActivity(this)
            }
        }
    }
}
