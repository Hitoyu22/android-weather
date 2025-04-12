package fr.esgi.android.weather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import fr.esgi.android.weather.R
import kotlin.reflect.KClass

abstract class WeatherActivity(val layout: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<NavigationBarView>(R.id.navigation).setOnItemSelectedListener { onNavBarClick(it) }

    }

    private fun onNavBarClick(item: MenuItem): Boolean {
        val activity: KClass<out AppCompatActivity> = when (item.itemId) {
            R.id.favorites -> {
                Log.d("Navigation", "Favorites")
                FavoritesActivity::class
            }
            R.id.home -> {
                Log.d("Navigation", "Home")
                HomeActivity::class
            }
            R.id.settings -> {
                Log.d("Navigation", "Settings")
                SettingsActivity::class
            }
            else -> return false
        }
        if (javaClass === activity.java) return true
        startActivity(Intent(this, activity.java))
        return true
    }

}