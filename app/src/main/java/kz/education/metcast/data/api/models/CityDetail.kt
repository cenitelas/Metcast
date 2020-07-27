package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityDetail (
    val city: City,
    var list: ArrayList<List>
): Parcelable