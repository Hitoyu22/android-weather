package fr.esgi.android.weather.network

import fr.esgi.android.weather.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "a20363091a1ab32d570b5563312ed343",
        @Query("units") units: String = "metric"
    ): WeatherResponse


    @GET("data/2.5/weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = "a20363091a1ab32d570b5563312ed343",
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
