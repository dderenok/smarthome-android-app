package com.example.smarthome.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.OnConflictStrategy.IGNORE
import com.example.smarthome.database.entity.Sensor
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Insert(onConflict = IGNORE)
    fun insert(sensor: Sensor)

    @Query("SELECT * from sensor WHERE name like :name")
    fun findByName(name: String): List<Sensor>

    @Update
    fun update(sensor: Sensor)

    @Delete
    fun delete(sensor: Sensor)

    @Query("DELETE FROM sensor")
    fun deleteAll()

    @Query("SELECT * FROM sensor ORDER BY name")
    fun getAll(): Flow<List<Sensor>>
}
