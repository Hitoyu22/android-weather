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
            Log.w(TAG, "Permission not granted")
            callback.onLocationUnavailable()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(TAG, "Last location found: ${location.latitude}, ${location.longitude}")
                    callback.onLocationResult(location)
                } else {
                    Log.w(TAG, "Last location is null, requesting update...")
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
                                    Log.d(TAG, "Location request success: ${loc.latitude}, ${loc.longitude}")
                                    callback.onLocationResult(loc)
                                } else {
                                    Log.e(TAG, "Location result is still null")
                                    callback.onLocationUnavailable()
                                }
                            }

                            override fun onLocationAvailability(availability: LocationAvailability) {
                                super.onLocationAvailability(availability)
                                if (!availability.isLocationAvailable) {
                                    Log.e(TAG, "Location not available")
                                    callback.onLocationUnavailable()
                                }
                            }
                        },
                        Looper.getMainLooper()
                    )
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error retrieving location: ${it.localizedMessage}")
                callback.onLocationUnavailable()
            }
    }
}