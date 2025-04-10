package fr.esgi.android.weather.workers

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.esgi.android.weather.network.WeatherAPI
import fr.esgi.android.weather.notifications.NotificationHelper

class WeatherWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            val response = WeatherAPI.getWeather("Paris", null)
            if (response.get().weather.any { it.main.contains("Thunderstorm", ignoreCase = true) }) {
                NotificationHelper(applicationContext).sendNotification(
                    "Alerte Orage", "Un orage approche, restez prudents !"
                )
            }
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}