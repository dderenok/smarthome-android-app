package com.example.smarthome.model

import com.example.smarthome.database.entity.Sensor
import com.example.smarthome.database.enumeration.SensorType

data class SensorDto(
    var id: Long,
    var name: String,
    var room: RoomDto,
    var sensorValue: Double,
    var sensorType: SensorType
) {
    companion object {
        fun fromEntity(sensor: Sensor) = SensorDto(
            id = sensor.id,
            name = sensor.name,
            room = RoomDto(name = sensor.roomName, amountOfSensors = 0),
            sensorValue = sensor.sensorValue,
            sensorType = sensor.sensorType
        )
    }
}
