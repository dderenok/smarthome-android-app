package com.example.smarthome.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smarthome.model.SensorDto

@Entity(tableName = "sensor")
data class Sensor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var name: String,
    var roomName: String
) {
    companion object {
        fun toEntity(sensorDto: SensorDto) = Sensor(
            id = sensorDto.id,
            name = sensorDto.name,
            roomName = sensorDto.room.name
        )
    }
}
