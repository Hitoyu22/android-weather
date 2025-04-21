package fr.esgi.android.weather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.search.CityAdapter
import kotlin.reflect.KClass

abstract class WeatherActivity(val layout: Int, val navId: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        val adapter = CityAdapter(this)
        searchView.setAdapter(adapter)

        searchView.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = parent.adapter.getItem(position) as City
            Log.d("CitySelection", "Selected city: ${selectedCity.name}")

            val intent = Intent(this, CityDetailActivity::class.java)
            intent.putExtra("CITY_NAME", selectedCity.name)
            startActivity(intent)
        }
    }
}
