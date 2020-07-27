package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class City(
    var id:Int,
    val name:String,
    val sunrise:String,
    val sunset:String,
    val timezone:String
):Parcelable