package kz.education.metcast.view.holders

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_view_days.view.*
import kz.education.metcast.data.api.models.List
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DayHolder(view : View) : RecyclerView.ViewHolder(view) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(list: List, position: Int) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val day = LocalDateTime.parse(list.dt_txt,formatter)
        day.plusHours(6)
        itemView.view_holder_day_name.text = day.dayOfWeek.toString().subSequence(0,3)
        Glide.with(itemView).load("${"https://openweathermap.org/img/wn/"}${list.weather[0].icon}@2x.png").into(itemView.view_holder_day_icon)
        itemView.view_holder_day_temp.text = (list.main.temp.toFloat()- 273.15F).toInt().toString()+" ℃"
    }
}