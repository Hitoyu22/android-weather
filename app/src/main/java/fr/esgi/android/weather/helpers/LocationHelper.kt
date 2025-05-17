package fr.esgi.android.weather.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

object LocationHelper {

    interface LocationCallbackListener {
        fun onLocationResult(location: Location)
        fun onLocationUnavailable()
    }

    fun getLastKnownLocation(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        callback: LocationCallbackListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback.onLocationUnavailable()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    callback.onLocationResult(location)
                    return@addOnSuccessListener
                }

                val locationRequest = LocationRequest.Builder(0).apply {
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    setWaitForAccurateLocation(false)
                    setMinUpdateIntervalMillis(0)
                    setMaxUpdates(1)
                }.build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            val loc = result.lastLocation
                            if (loc != null) callback.onLocationResult(loc)
                            else callback.onLocationUnavailable()
                        }

                        override fun onLocationAvailability(availability: LocationAvailability) {
                            super.onLocationAvailability(availability)
                            if (!availability.isLocationAvailable) {
                                callback.onLocationUnavailable()
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            }
            .addOnFailureListener { callback.onLocationUnavailable() }
    }
}