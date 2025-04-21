package fr.esgi.android.weather.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather

class FavoritesAdapter(val favorites: Map<City, Weather>, private val cities: List<City> = ArrayList(favorites.keys)) : RecyclerView.Adapter<FavoritesAdapter.MainViewHolder>() {

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
        holder.icon.text = weather.weather.icon
        holder.temperature.text = "${weather.temperature}°C"
    }

    override fun getItemCount(): Int = cities.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val city: TextView = view.findViewById<TextView>(R.id.city)
        val description: TextView = view.findViewById<TextView>(R.id.description)
        val icon: TextView = view.findViewById<TextView>(R.id.icon)
        val temperature: TextView = view.findViewById<TextView>(R.id.temperature)
    }
}