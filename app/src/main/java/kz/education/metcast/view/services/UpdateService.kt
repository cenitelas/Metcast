package kz.education.metcast.view.services

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.google.gson.Gson
import kz.education.metcast.R
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.data.dao.AppDatabase
import kz.education.metcast.utils.CityUtils
import kz.education.metcast.view.widgets.WidgetMetcast


class UpdateService : Service() {

    override fun onBind(intent:Intent): IBinder? {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {
        var view = RemoteViews(getPackageName(), R.layout.widget_metcast);
        var cityUtils = CityUtils()
        cityUtils.updateCity(view,this)
        return super.onStartCommand(intent, flags, startId);
    }
}
