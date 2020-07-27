package kz.education.metcast.data.dao.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityDetailEntity(
    @PrimaryKey
    var id:Int,
    var data:String
)