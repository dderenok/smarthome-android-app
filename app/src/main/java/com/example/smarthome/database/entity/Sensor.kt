package com.example.smarthome.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.smarthome.database.converters.SensorTypeConverter
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.model.SensorDto

@Entity(tableName = "sensor")
@TypeConverters(SensorTypeConverter::class)
data class Sensor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var name: String,
    var roomName: String,
    var sensorValue: Double,
    var sensorType: SensorType
) {
    companion object {
        fun toEntity(sensorDto: SensorDto) = Sensor(
            id = sensorDto.id,
            name = sensorDto.name,
            roomName = sensorDto.room.name,
            sensorValue = sensorDto.sensorValue,
            sensorType = sensorDto.sensorType
        )
    }
}
