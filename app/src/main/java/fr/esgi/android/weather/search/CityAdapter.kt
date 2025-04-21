package fr.esgi.android.weather.search

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

class CityAdapter(context: Context) : ArrayAdapter<City>(context, R.layout.item_city), Filterable {

    private var cities: List<City> = emptyList()

    override fun getCount(): Int {
        return cities.size
    }

    override fun getItem(position: Int): City? {
        return cities[position]
    }

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
                    val futureCities = WeatherAPI.searchCity(constraint.toString())
                    val cities = futureCities.get()
                    val limitedCities = cities.take(3)
                    results.values = limitedCities
                    results.count = limitedCities.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    cities = results.values as List<*> as List<City>
                    notifyDataSetChanged()
                } else {
                    cities = emptyList()
                    notifyDataSetChanged()
                }
            }
        }
    }
}
