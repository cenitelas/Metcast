package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather (
    val main:String,
    val description:String,
    val icon:String
):Parcelable