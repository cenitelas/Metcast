package kz.education.metcast.view.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_city_info.*
import kz.education.metcast.R
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.data.api.models.List
import kz.education.metcast.utils.OnSwipeTouchListener
import kz.education.metcast.view.adapter.DaysAdapter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList


class FragmentCityInfo : Fragment() {
    var show = true;
    private var mAdapter: DaysAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_info, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListeners()
        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onChangeCity(cityDetail:CityDetail?){
        initializeView(cityDetail)
        var fragments = fragmentManager!!.fragments
        ObjectAnimator.ofFloat(   fragments[0].view!!.parent, "translationX",0F).start();
        ObjectAnimator.ofFloat(   view!!.parent, "translationX",0F).start();
        show=true;
    }

    private fun initRecyclerView() {

        fragment_city_rv_list_days.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(view!!.context,LinearLayoutManager.HORIZONTAL,false)
        fragment_city_rv_list_days.layoutManager = layoutManager
    }

    private fun initializeListeners(){
        fragment_city_info.setOnTouchListener(object: OnSwipeTouchListener(this.context) {
            override fun onSwipeUp() {
                var fragments = fragmentManager!!.fragments
                (fragments[0] as FragmentCities).updateCityInfo()
            }
            override fun onSwipeDown() {
                var fragments = fragmentManager!!.fragments
                (fragments[0] as FragmentCities).updateCityInfo()
            }
        })
        fragment_city_info_menu.setOnClickListener{
            var fragments = fragmentManager!!.fragments
            fragments[0].view!!.setPadding(fragment_city_info_menu.width.toInt(),0,0,0)
            if(show) {
                ObjectAnimator.ofFloat(   fragments[0].view!!.parent, "translationX",fragments[0].view!!.width.toFloat()-fragment_city_info_menu.width.toFloat()).start();
                ObjectAnimator.ofFloat(   view!!.parent, "translationX",fragments[0].view!!.width.toFloat()-fragment_city_info_menu.width.toFloat()).start();
                show=false;
            }else{
                ObjectAnimator.ofFloat(   fragments[0].view!!.parent, "translationX",0F).start();
                ObjectAnimator.ofFloat(   view!!.parent, "translationX",0F).start();
                show=true;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeView(cityDetail: CityDetail?){
        if(cityDetail!=null){
            fragment_city_info_city_name.text = cityDetail.city.name;
            val list = ArrayList<List>();
            fragment_city_info_city_null.visibility = View.INVISIBLE;
            cityDetail.list.forEach {
                if(it.dt_txt.split(' ')[1]=="12:00:00"){
                    list.add(it);
                }
            }
            mAdapter = DaysAdapter(list)
            fragment_city_rv_list_days.adapter = mAdapter
            fragment_city_info_city_null.visibility = View.INVISIBLE;
            Glide.with(this).load("${"https://openweathermap.org/img/wn/"}${cityDetail.list[0].weather[0].icon}@4x.png").into(fragment_city_info_city_icon);
            fragment_city_info_city_weather.text = cityDetail.list[0].weather[0].description;
            fragment_city_info_city_temp.text = (cityDetail.list[0].main.temp.toFloat()- 273.15F).toInt().toString()+" ℃";
            fragment_city_info_city_hpa.text = cityDetail.list[0].main.pressure+" hpa";
            fragment_city_info_city_humidity.text = cityDetail.list[0].main.humidity.toInt().toString()+" %";
            fragment_city_info_city_wind.text = cityDetail.list[0].wind.speed.toFloat().toInt().toString()+" m/s";
            val date =  LocalDateTime.now();
            fragment_city_info_last_update.text = "LAST UPDATE ${date.dayOfWeek.toString().substring(0,3)} ${date.dayOfMonth} ${date.month.toString().substring(0,3)} ${date.year} ${date.hour}:${date.minute}"
            val sunrise = LocalDateTime.ofInstant(Instant.ofEpochMilli(cityDetail.city.sunrise.toLong()), ZoneId.systemDefault());
            val sunset = Date(cityDetail.city.sunset.toLong());
            fragment_city_info_city_days_sunrice.text = "${sunrise.minusHours(12).hour}:${sunrise.minute} sunrise"
            fragment_city_info_city_days_sunset.text = "${sunset.hours}:${sunset.minutes} sunset"
        }else{
            fragment_city_info_city_null.visibility = View.VISIBLE;
        }
    }
}