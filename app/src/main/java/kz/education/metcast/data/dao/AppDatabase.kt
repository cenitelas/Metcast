package kz.education.metcast.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.education.metcast.data.dao.entities.CityDetailCurrentEntity
import kz.education.metcast.data.dao.entities.CityDetailEntity

@Database(entities = [CityDetailEntity::class,CityDetailCurrentEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDetailDao():CityDetailDao
    abstract fun cityDetailCurrentDao():CityDetailCurrentDao
}