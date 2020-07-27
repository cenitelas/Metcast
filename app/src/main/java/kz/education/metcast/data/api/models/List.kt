package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class List(
    val dt:String,
    val dt_txt:String,
    val main: Main,
    var weather: ArrayList<Weather>,
    val wind: Wind
):Parcelable