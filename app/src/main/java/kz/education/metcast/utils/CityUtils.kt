package kz.education.metcast.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kz.education.metcast.R
import kz.education.metcast.data.api.RequestInterface
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.data.dao.AppDatabase
import kz.education.metcast.view.fragments.FragmentCities
import kz.education.metcast.view.widgets.WidgetMetcast
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



class CityUtils: Disposable {
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var remoteViews:RemoteViews;
    private lateinit var remoteContext:Context;

    val BASE_URL = "https://api.openweathermap.org"

    val API_KEY = "42eb0dd98036aa8239169f3f56ab47b6"



    fun addCity(fragment: Fragment){
        var alertDialog = fragment.activity?.let { AlertDialog.Builder(it) };
        alertDialog?.setTitle("Добавление города");
        alertDialog?.setMessage("Введите название города");

        var input = EditText(fragment.activity);
        var lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog?.setView(input);


        alertDialog?.setPositiveButton("Добавить") { dialog, which ->
            var city = input.getText().toString();
            if (city.length>0) {
                (fragment as FragmentCities).addCity(city);
            }
        }
        alertDialog?.setNegativeButton("Отменить") { dialog, which ->
            dialog.cancel();
        };
        alertDialog?.show();
    }

    fun updateCity(view: RemoteViews,context: Context){
        remoteViews=view;
        remoteContext=context;
        mCompositeDisposable = CompositeDisposable()
        var db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase")
            .allowMainThreadQueries()
            .build()
        println("updateCity")
        var currentCity = db.cityDetailCurrentDao().getCityDetailById(1);
        if(currentCity!=null) {
            println("currentCity")
            val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

            mCompositeDisposable?.add(
                requestInterface.getCityById(currentCity.idCurrent.toString(), API_KEY, "ru")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::updateRemoteView, this::updateRemoteViewError))
        }else{
            view.setTextViewText(R.id.widget_loading, "Выберите город");
        }
    }

     fun updateRemoteView(cityDetail: CityDetail){
        println("updateRemoteView")
        remoteViews.setTextViewText(R.id.widget_name, cityDetail?.city?.name);
        Glide.with(remoteContext).asBitmap().load("${"https://openweathermap.org/img/wn/"}${cityDetail.list[0].weather[0].icon}@2x.png").into(object : SimpleTarget<Bitmap>(100, 100) {
            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                remoteViews.setImageViewBitmap(R.id.widget_city_icon,resource)
            }
        })
        remoteViews.setTextViewText(R.id.widget_loading, "");
        remoteViews.setTextViewText(R.id.widget_temp, (cityDetail.list[0].main.temp.toFloat()- 273.15F).toInt().toString()+" ℃");
        remoteViews.setTextViewText(R.id.widget_weather,cityDetail.list[0].weather[0].main)
        var theWidget = ComponentName(remoteContext, WidgetMetcast::class.java);
        var manager = AppWidgetManager.getInstance(remoteContext);
        manager.updateAppWidget(theWidget, remoteViews);
    }

     fun updateRemoteViewError(error:Throwable){

    }

    override fun isDisposed(): Boolean {
        return mCompositeDisposable!=null
    }

    override fun dispose() {
        if(this.isDisposed)
        mCompositeDisposable?.clear()
    }
}