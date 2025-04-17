package fr.esgi.android.weather.widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import fr.esgi.android.weather.R

class Widget1Provider : AppWidgetProvider() {
    @SuppressLint("RemoteViewLayout")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val location = "Paris, France"
        val temperature = "15°C"

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget1_layout)
            views.setTextViewText(R.id.location, location)
            views.setTextViewText(R.id.temperature, temperature)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}