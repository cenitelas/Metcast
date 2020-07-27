package kz.education.metcast.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.education.metcast.R
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.view.holders.CityHolder

class CitiesAdapter (private val dataList : ArrayList<CityDetail>, private val listener : Listener) : RecyclerView.Adapter<CityHolder>() {

    interface Listener {

        fun onItemClick(citeDetail: CityDetail)

        fun deleteCity(citeDetail: CityDetail)
    }

    private val colors : Array<String> = arrayOf("#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5")

    override fun onBindViewHolder(holder: CityHolder, position: Int) {

        holder.bind(dataList[position], listener, colors, position)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_cities, parent, false)

        return CityHolder(view)
    }
}