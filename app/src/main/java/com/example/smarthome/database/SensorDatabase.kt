package com.example.smarthome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smarthome.database.dao.SensorDao
import com.example.smarthome.database.entity.Sensor
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

            val sensorOne = Sensor(id = 1, name = "HighLight", roomName = "Kitchen")
            val sensorSecond = Sensor(id = 2, name = "Temperature_1", roomName = "Bathroom")
            val sensorThird = Sensor(id = 3, name = "Temperature_2", roomName = "Kitchen")
            val sensorFouth = Sensor(id = 4, name = "Light_1", roomName = "Hall")
            val sensorFifth = Sensor(id = 5, name = "Temperature_3", roomName = "Hall")
            sensorDao.insert(sensorOne)
            sensorDao.insert(sensorSecond)
            sensorDao.insert(sensorThird)
            sensorDao.insert(sensorFouth)
            sensorDao.insert(sensorFifth)

            // TODO: Add your own words!
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
