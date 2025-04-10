package fr.esgi.android.weather.network

import android.util.Log
import com.google.gson.Gson
import fr.esgi.android.weather.models.WeatherResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

object WeatherAPI {

    private const val WEATHER_API = "https://api.openweathermap.org/"
    private const val API_KEY = "a20363091a1ab32d570b5563312ed343"

    private fun <T> request(endpoint: String, classOfT: Class<T>): CompletableFuture<T> {
        val con = URL("$WEATHER_API$endpoint&appid=$API_KEY&lang=fr").openConnection() as HttpURLConnection
        con.setRequestProperty("Content-Type", "application/json")

        return CompletableFuture.supplyAsync {
            val reader = BufferedReader(InputStreamReader(con.inputStream))

            val json = Gson().fromJson(reader, classOfT)
            Log.d("Network", "GET request \"$WEATHER_API$endpoint\": $json")
            return@supplyAsync json
        }
    }

    fun getWeather(city: String, units: String?): CompletableFuture<WeatherResponse> {
        return request("data/2.5/weather?q=$city&units=" + (units ?: "metric"), WeatherResponse::class.java)
    }

    fun getWeatherByCoordinates(lat: Double, lon: Double, units: String?): CompletableFuture<WeatherResponse> {
        return request("data/2.5/weather?lat=$lat&lon=$lon&units=" + (units ?: "metric"), WeatherResponse::class.java)
    }

}