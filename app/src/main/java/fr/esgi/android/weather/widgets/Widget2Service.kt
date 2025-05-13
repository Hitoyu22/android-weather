package fr.esgi.android.weather.widgets

import android.content.Intent
import android.widget.RemoteViewsService

class Widget2Service : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return Widget2RemoteViewsFactory(this.applicationContext)
    }
}
