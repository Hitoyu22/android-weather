package fr.esgi.android.weather.widgets

import android.content.Intent
import android.widget.RemoteViewsService

class Widget1Service : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return Widget1RemoteViewsFactory(this.applicationContext)
    }
}