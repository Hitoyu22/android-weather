package fr.esgi.android.weather.api

import fr.esgi.android.weather.R

enum class WeatherSource(
    val id: String,
    val stringId: Int
) {

    DWD_GERMANY("icon_seamless", R.string.dwd_germany),
    NOAA_US("gfs_seamless", R.string.noaa_us),
    METEO_FRANCE("meteofrance_seamless", R.string.meteo_france),
    ECMWF("ecmwf_ifs025", R.string.ecmwf),
    UK_MET_OFFICE("ukmo_seamless", R.string.uk_met_office),
    KMA_KOREA("kma_seamless", R.string.kma_korea),
    JMA_JAPAN("jma_seamless", R.string.jma_japan),
    MET_NORWAY("metno_seamless", R.string.met_norway),
    GEM_CANADA("gem_seamless", R.string.gem_canada),
    BOM_AUSTRALIA("bom_access_global", R.string.bom_australia),
    CMA_CHINA("cma_grapes_global", R.string.cma_china),
    KNMI_NETHERLANDS("knmi_seamless", R.string.knmi_netherlands),
    DMI_DENMARK("dmi_seamless", R.string.dmi_denmark),
    ITALIAMETEO("italia_meteo_arpae_icon_2i", R.string.italiameteo);

    companion object {
        fun get(name: String?): WeatherSource {
            if (name == null) return METEO_FRANCE
            return entries.find { it.id == name } ?: METEO_FRANCE
        }
    }

}