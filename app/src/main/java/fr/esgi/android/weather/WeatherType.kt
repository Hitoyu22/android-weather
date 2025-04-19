package fr.esgi.android.weather

enum class WeatherType(private val day: Int, private val night: Int, val icon: String, private vararg val codes: Int) {

    // https://github.com/open-meteo/open-meteo/issues/789#issuecomment-2206144530
    SUNNY(R.drawable.sunny, R.drawable.sunny_night, "☀️", 0, 1),
    CLOUDY(R.drawable.cloudy, R.drawable.cloudy_night, "⛅", 2, 3, 45, 48),
    RAINY(R.drawable.rainy, R.drawable.rainy_night, "🌧️", 51, 53, 55, 80, 81, 82, 61, 63, 65, 56, 57, 66, 67),
    SNOW(R.drawable.snow, R.drawable.snow_night, "🌨️", 77, 85, 86, 71, 73, 75),
    THUNDER(R.drawable.thunder, R.drawable.thunder_night, "⛈️", 95, 96, 99);

    companion object {
        fun getFromCode(code: Int?): WeatherType {
            if (code == null) return SUNNY
            return WeatherType.entries.find { it.codes.contains(code) } ?: SUNNY
        }
    }

    fun getResourceID(darkMode: Boolean): Int {
        return if (darkMode) night else day
    }

}