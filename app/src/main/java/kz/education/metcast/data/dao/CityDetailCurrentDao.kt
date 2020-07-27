package kz.education.metcast.data.dao

import androidx.room.*
import kz.education.metcast.data.dao.entities.CityDetailCurrentEntity

@Dao
interface CityDetailCurrentDao {
    @Query("SELECT * FROM CityDetailCurrentEntity WHERE id == :id")
    fun getCityDetailById(id: Int): CityDetailCurrentEntity

    @Query("SELECT * FROM CityDetailCurrentEntity")
    fun getCitiesDetails(): List<CityDetailCurrentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CityDetailCurrentEntity)

    @Update
    fun update(entity: CityDetailCurrentEntity)

    @Delete
    fun delete(entity: CityDetailCurrentEntity)
}