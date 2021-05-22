package com.example.smarthome.database.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.smarthome.database.entity.Sensor
import com.example.smarthome.database.repository.SensorRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SensorViewModel(private val sensorRepository: SensorRepository) : ViewModel() {
    val allSensors: LiveData<List<Sensor>> = sensorRepository.allSensors.asLiveData()

    fun insert(sensor: Sensor) {
        viewModelScope.launch {
            sensorRepository.insert(sensor)
        }
    }

    fun findByName(name: String): List<Sensor> {
        return sensorRepository.findByName(name)
    }

    fun update(sensor: Sensor) {
        viewModelScope.launch {
            sensorRepository.update(sensor)
        }
    }

    fun delete(sensor: Sensor) {
        viewModelScope.launch {
            sensorRepository.delete(sensor)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            sensorRepository.deleteAll()
        }
    }
}

class SensorViewModelFactory(private val repository: SensorRepository) : Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            return SensorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
