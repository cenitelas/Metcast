package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Main(
    val temp:String,
    val humidity:String,
    val pressure:String
):Parcelable