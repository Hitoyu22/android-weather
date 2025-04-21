package fr.esgi.android.weather.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

object LocationHelper {

    private const val TAG = "LocationHelper"

    interface LocationCallbackListener {
        fun onLocationResult(location: Location)
        fun onLocationUnavailable()
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        callback: LocationCallbackListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
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
                } else {
                    val locationRequest = LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        0
                    ).apply {
                        setWaitForAccurateLocation(false)
                        setMinUpdateIntervalMillis(0)
                        setMaxUpdates(1)
                    }.build()

                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                val loc = result.lastLocation
                                if (loc != null) {
                                    callback.onLocationResult(loc)
                                } else {
                                    callback.onLocationUnavailable()
                                }
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
            }
            .addOnFailureListener {
                callback.onLocationUnavailable()
            }
    }
}