package fr.esgi.android.weather

enum class WeatherType(private val day: Int, private val night: Int, private val icon: Int, val iconText: String, private vararg val codes: Int) {

    SUNNY(R.drawable.sunny, R.drawable.sunny_night, R.drawable.sunny_icon, "☀️", 0, 1),
    CLOUDY(R.drawable.cloudy, R.drawable.cloudy_night, R.drawable.cloudy_icon, "⛅", 2, 3, 45, 48),
    RAINY(R.drawable.rainy, R.drawable.rainy_night, R.drawable.rainy_icon, "🌧️", 51, 53, 55, 80, 81, 82, 61, 63, 65, 56, 57, 66, 67),
    SNOW(R.drawable.snow, R.drawable.snow_night, R.drawable.snow_icon, "🌨️", 77, 85, 86, 71, 73, 75),
    THUNDER(R.drawable.thunder, R.drawable.thunder_night, R.drawable.thunder_icon, "⛈️", 95, 96, 99);

    companion object {
        fun getFromCode(code: Int?): WeatherType {
            if (code == null) return SUNNY
            return WeatherType.entries.find { it.codes.contains(code) } ?: SUNNY
        }
    }

    fun getWidgetIcon(): Int {
        return icon
    }

    fun getResourceID(darkMode: Boolean): Int {
        return if (darkMode) night else day
    }
}
