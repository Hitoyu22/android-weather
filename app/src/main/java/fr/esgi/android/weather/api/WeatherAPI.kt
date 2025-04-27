package fr.esgi.android.weather.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import fr.esgi.android.weather.WeatherType
import fr.esgi.android.weather.api.models.City
import fr.esgi.android.weather.api.models.Weather
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

object WeatherAPI {

    private const val GEO_API = "https://geocoding-api.open-meteo.com/v1/"
    private const val CITY_API = "https://nominatim.openstreetmap.org/"
    private const val WEATHER_API = "https://api.open-meteo.com/v1/"
    private const val AIR_API = "https://air-quality-api.open-meteo.com/v1/"
    private val gson = Gson()
    private val cache = HashMap<String, Object>()

    private fun <T> request(api: String, endpoint: String, classOfT: Class<T>): CompletableFuture<T> {
        val url = api + endpoint
        @Suppress("UNCHECKED_CAST")
        if (cache.contains(url)) return CompletableFuture.completedFuture<T>(cache.get(url) as T)

        val con = URL(url).openConnection()
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty("User-Agent", "ESGI-Weather-App/1.0")

        return CompletableFuture.supplyAsync {
            val reader = BufferedReader(InputStreamReader(con.inputStream))

            val json = gson.fromJson(reader, classOfT)
            cache.put(url, json as Object)
            Log.d("Network", "GET request \"$url\": $json")
            json
        }
    }

    fun searchCities(city: String): CompletableFuture<List<City>> {
        return request(GEO_API, "search?name=$city", JsonObject::class.java).thenApplyAsync {
            it.getAsJsonArray("results").map {
                gson.fromJson(it, City::class.java)
            }
        }
    }

    fun getCityFromCoordinates(latitude: Double, longitude: Double): CompletableFuture<City> {
        return request(CITY_API, "reverse?lat=$latitude&lon=$longitude&format=json&accept-language=fr", JsonObject::class.java).thenApplyAsync { json ->
            val address = json.getAsJsonObject("address")

            val city = when {
                address.has("city") -> address.get("city").asString
                address.has("city_district") -> address.get("city_district").asString
                address.has("town") -> address.get("town").asString
                address.has("village") -> address.get("village").asString
                address.has("hamlet") -> address.get("hamlet").asString
                else -> null
            }

            val country: String? = if (address.has("country")) address.get("country").asString else null
            City(city, country, latitude, longitude)
        }
    }

    fun getCurrentWeather(city: City): CompletableFuture<Weather> {
        return request(WEATHER_API, "forecast?latitude=${city.latitude}&longitude=${city.longitude}&current=temperature_2m,weather_code,is_day&models=meteofrance_seamless", JsonObject::class.java).thenApplyAsync {
            val current = it.getAsJsonObject("current")
            Weather(
                LocalDateTime.parse(current.get("time").asString).toLocalDate(),
                current.get("temperature_2m").asDouble,
                current.get("is_day").asInt == 1,
                WeatherType.getFromCode(current.get("weather_code")?.asInt)
            )
        }
    }

    fun getWeather(city: City, start: String, end: String): CompletableFuture<List<Weather>> {
        return request(WEATHER_API, "forecast?latitude=${city.latitude}&longitude=${city.longitude}&daily=temperature_2m_max,weather_code&models=meteofrance_seamless&start_date=$start&end_date=$end", JsonObject::class.java).thenApplyAsync {
            val daily = it.getAsJsonObject("daily")

            val time = daily.getAsJsonArray("time")
            val weather = daily.getAsJsonArray("weather_code")
            val temperature = daily.getAsJsonArray("temperature_2m_max")

            val days = ArrayList<Weather>()
            for (i in 0 until time.size()) {
                days.add(Weather(
                    LocalDate.parse(time.get(i).asString),
                    if (temperature.get(i).isJsonNull) null else temperature.get(i).asDouble,
                    true,
                    WeatherType.getFromCode(if (weather.get(i).isJsonNull) null else weather.get(i).asInt)
                ))
            }
            days
        }
    }

    fun getAirQuality(city: City): CompletableFuture<Int> {
        return request(AIR_API, "air-quality?latitude=${city.latitude}&longitude=${city.longitude}&current=european_aqi&forecast_days=1", JsonObject::class.java).thenApplyAsync { json ->
            json.getAsJsonObject("current").get("european_aqi").asInt
        }
    }

}