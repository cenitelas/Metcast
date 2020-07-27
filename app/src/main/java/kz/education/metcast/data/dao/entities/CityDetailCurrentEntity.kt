package kz.education.metcast.data.dao.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class CityDetailCurrentEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var idCurrent:Int
)