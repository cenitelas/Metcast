package kz.education.metcast.data.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Wind(
    val speed:String
):Parcelable