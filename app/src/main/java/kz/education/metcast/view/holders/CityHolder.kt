package kz.education.metcast.view.holders

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_view_cities.view.*
import kz.education.metcast.R
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.view.CitiesAdapter


class CityHolder(view : View) : RecyclerView.ViewHolder(view) {

    fun bind(citeDetail: CityDetail, listener: CitiesAdapter.Listener, colors : Array<String>, position: Int) {
        val mDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.city_holder_panel)
        mDrawable!!.colorFilter = PorterDuffColorFilter(Color.parseColor(colors[position % 6]), PorterDuff.Mode.MULTIPLY)
        Glide.with(itemView).load("${"https://openweathermap.org/img/wn/"}${citeDetail.list[0].weather[0].icon}@2x.png").into(itemView.view_holder_city_icon);
        itemView.view_holder_city_name.text = citeDetail.city.name
        itemView.view_holder_city_temp.text = (citeDetail.list[0].main.temp.toFloat()- 273.15F).toInt().toString()+" ℃"
        itemView.view_holder_city_weather.text = citeDetail.list[0].weather[0].main
        itemView.background = mDrawable
        itemView.setOnClickListener{ listener.onItemClick(citeDetail) }
        itemView.view_holder_city_delete.setOnClickListener{listener.deleteCity(citeDetail)}
    }
}