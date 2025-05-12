package fr.esgi.android.weather.lists

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.activities.CityDetailActivity
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import fr.esgi.android.weather.WeatherApp

class FavoritesAdapter(
    private val favorites: MutableMap<City, Weather>,
    private val cities: MutableList<City> = ArrayList(favorites.keys),
    private val app: WeatherApp
) : RecyclerView.Adapter<FavoritesAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.favorites_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val city = cities[position]
        val weather = favorites[city]!!

        holder.city.text = "${city.name}, ${city.country}"
        holder.description.text = weather.weather.toString()
        holder.icon.text = weather.weather.iconText
        holder.temperature.text = "${weather.temperature}°C"

        holder.deleteButton.setOnClickListener {
            favorites.remove(city)
            cities.removeAt(position)

            val updatedFavorites = app.getFavorites().toMutableList().apply {
                remove(city)
            }
            app.setFavorites(updatedFavorites)

            notifyItemRemoved(position)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Intent(context, CityDetailActivity::class.java).apply {
                putExtra("CITY_LAT", city.latitude)
                putExtra("CITY_LON", city.longitude)
                context.startActivity(this)
            }
        }
    }

    override fun getItemCount(): Int = cities.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val city: TextView = view.findViewById(R.id.city)
        val description: TextView = view.findViewById(R.id.description)
        val icon: TextView = view.findViewById(R.id.icon)
        val temperature: TextView = view.findViewById(R.id.temperature)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }
}
