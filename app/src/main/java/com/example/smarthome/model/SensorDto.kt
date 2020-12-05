package com.example.smarthome.model

import com.example.smarthome.database.entity.Sensor

data class SensorDto(
    var id: Long,
    var name: String,
    var room: RoomDto
) {
    companion object {
        fun fromEntity(sensor: Sensor) = SensorDto(
            id = sensor.id,
            name = sensor.name,
            room = RoomDto(name = sensor.roomName, amountOfSensors = 0)
        )
    }
}
