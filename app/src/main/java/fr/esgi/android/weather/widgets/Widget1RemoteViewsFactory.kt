package fr.esgi.android.weather.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import fr.esgi.android.weather.R

class Widget1RemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var dataList: List<String> = listOf("Paris, France", "15°C")

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun getCount(): Int = dataList.size

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget1_layout)
        views.setTextViewText(R.id.location, dataList[0])
        views.setTextViewText(R.id.temperature, dataList[1])
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun onDataSetChanged() {
        dataList = listOf("Paris, France", "15°C")
    }
}