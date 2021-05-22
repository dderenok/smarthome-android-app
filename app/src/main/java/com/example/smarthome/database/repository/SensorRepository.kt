package com.example.smarthome.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.smarthome.database.dao.SensorDao
import com.example.smarthome.database.entity.Sensor
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

@Suppress("RedundantSuspendModifier")
class SensorRepository(private var sensorDao: SensorDao) {
    var allSensors: Flow<List<Sensor>> = sensorDao.getAll().flowOn(Default)
        .conflate()

    @WorkerThread
    suspend fun insert(sensor: Sensor) {
        Thread {
            sensorDao.insert(sensor)
        }.start()
    }

    @WorkerThread
    fun findByName(name: String): List<Sensor> {
        var sensor: List<Sensor> = listOf()
        Thread {
             sensor = sensorDao.findByName(name)
        }.start()
        return sensor
    }

    @WorkerThread
    suspend fun update(sensor: Sensor) {
        Thread {
            sensorDao.update(sensor)
        }.start()
    }

    @WorkerThread
    suspend fun delete(sensor: Sensor) {
        Thread {
            sensorDao.delete(sensor)
        }.start()
    }

    @WorkerThread
    suspend fun deleteAll() {
        Thread {
            sensorDao.deleteAll()
        }.start()
    }
}
