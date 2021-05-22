package com.example.smarthome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smarthome.database.dao.SensorDao
import com.example.smarthome.database.entity.Sensor
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.database.enumeration.SensorType.LIGHT
import com.example.smarthome.database.enumeration.SensorType.TEMPERATURE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Sensor::class], version = 1)
abstract class SensorDatabase : RoomDatabase() {
    abstract fun sensorDao(): SensorDao

    private class SensorDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.sensorDao())
                }
            }
        }

        suspend fun populateDatabase(sensorDao: SensorDao) {
            sensorDao.deleteAll()

            val sensorOne = Sensor(id = 1, name = "Light", roomName = "Kitchen", sensorType = LIGHT, sensorValue = 0.0)
            val sensorSecond = Sensor(id = 2, name = "DHT11_2", roomName = "Bathroom", sensorType = TEMPERATURE, sensorValue = 23.2)
            sensorDao.insert(sensorOne)
            sensorDao.insert(sensorSecond)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SensorDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SensorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    SensorDatabase::class.java,
                    "sensor_database"
                )
                    .addCallback(SensorDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                
                instance
            }
        }
    }
}
