package com.example.smarthome.database

import android.app.Application
import com.example.smarthome.database.repository.SensorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SensorApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { SensorDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { SensorRepository(database.sensorDao()) }
}
