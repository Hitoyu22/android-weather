package fr.esgi.android.weather.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    @SerializedName("current") val current: CurrentWeather,
    @SerializedName("minutely") val minutely: List<MinutelyForecast>?,
    @SerializedName("hourly") val hourly: List<HourlyForecast>?,
    @SerializedName("daily") val daily: List<DailyForecast>?,
    @SerializedName("alerts") val alerts: List<WeatherAlert>?
)

data class CurrentWeather(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("clouds") val cloudiness: Int,
    @SerializedName("uvi") val uvIndex: Double,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double?,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("rain") val rain: Precipitation?,
    @SerializedName("snow") val snow: Precipitation?,
    @SerializedName("weather") val weather: List<Weather>
)

data class MinutelyForecast(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("precipitation") val precipitation: Double
)

data class HourlyForecast(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("clouds") val cloudiness: Int,
    @SerializedName("uvi") val uvIndex: Double,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double?,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("pop") val probabilityOfPrecipitation: Double,
    @SerializedName("rain") val rain: Precipitation?,
    @SerializedName("snow") val snow: Precipitation?,
    @SerializedName("weather") val weather: List<Weather>
)

data class DailyForecast(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?,
    @SerializedName("moonrise") val moonrise: Long?,
    @SerializedName("moonset") val moonset: Long?,
    @SerializedName("moon_phase") val moonPhase: Double,
    @SerializedName("temp") val temperature: Temperature,
    @SerializedName("feels_like") val feelsLike: FeelsLikeTemperature,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double?,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("clouds") val cloudiness: Int,
    @SerializedName("uvi") val uvIndex: Double,
    @SerializedName("pop") val probabilityOfPrecipitation: Double,
    @SerializedName("rain") val rain: Double?,
    @SerializedName("snow") val snow: Double?,
    @SerializedName("weather") val weather: List<Weather>
)

data class Temperature(
    @SerializedName("morn") val morning: Double,
    @SerializedName("day") val day: Double,
    @SerializedName("eve") val evening: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double
)

data class FeelsLikeTemperature(
    @SerializedName("morn") val morning: Double,
    @SerializedName("day") val day: Double,
    @SerializedName("eve") val evening: Double,
    @SerializedName("night") val night: Double
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Precipitation(
    @SerializedName("1h") val oneHour: Double?
)

data class WeatherAlert(
    @SerializedName("sender_name") val senderName: String,
    @SerializedName("event") val event: String,
    @SerializedName("start") val start: Long,
    @SerializedName("end") val end: Long,
    @SerializedName("description") val description: String,
    @SerializedName("tags") val tags: List<String>
)
