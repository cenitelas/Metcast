package kz.education.metcast.data.dao

import androidx.room.*
import kz.education.metcast.data.dao.entities.CityDetailEntity

@Dao
interface CityDetailDao {
    @Query("SELECT * FROM CityDetailEntity WHERE id == :id")
    fun getCityDetailById(id: Int): CityDetailEntity

    @Query("SELECT * FROM CityDetailEntity")
    fun getCitiesDetails(): List<CityDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CityDetailEntity)

    @Update
    fun update(entity: CityDetailEntity)

    @Delete
    fun delete(entity: CityDetailEntity)
}