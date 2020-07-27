package kz.education.metcast.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kz.education.metcast.R
import kz.education.metcast.data.api.models.List
import kz.education.metcast.view.holders.DayHolder

class DaysAdapter(private val dataList : ArrayList<List>) : RecyclerView.Adapter<DayHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayHolder, position: Int) {

        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_days, parent, false)

        return DayHolder(view)
    }
}