package fr.esgi.android.weather.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import fr.esgi.android.weather.R
import fr.esgi.android.weather.api.WeatherAPI
import fr.esgi.android.weather.api.models.City

class CitySearchAdapter(context: Context) : ArrayAdapter<City>(context, R.layout.item_city), Filterable {

    private var cities: List<City> = emptyList()

    override fun getCount(): Int = cities.size.coerceAtMost(3)

    override fun getItem(position: Int): City? = cities[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
        val city = getItem(position)
        view.findViewById<TextView>(R.id.cityName).text = city?.name
        view.findViewById<TextView>(R.id.cityCountry).text = city?.country
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint != null && constraint.length > 2) {
                    val futureCities = WeatherAPI.searchCities(constraint.toString())
                    val cities = futureCities.get()
                    results.values = cities
                    results.count = cities.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    @Suppress("UNCHECKED_CAST")
                    cities = results.values as List<City>
                    notifyDataSetChanged()
                } else {
                    cities = emptyList()
                    notifyDataSetChanged()
                }
            }
        }
    }
}