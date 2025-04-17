package fr.esgi.android.weather.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import fr.esgi.android.weather.R

class Widget1RemoteViewsFactory(private val context: Context, private val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private var dataList: List<String> = listOf("Paris, France", "15°C")

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return dataList.size
    }

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget1_layout)
        views.setTextViewText(R.id.location, dataList[0])
        views.setTextViewText(R.id.temperature, dataList[1])
        return views
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        dataList = listOf("Paris, France", "15°C")
    }
}