package com.example.smarthome.database.converters

import androidx.room.TypeConverter
import com.example.smarthome.database.enumeration.SensorType

class SensorTypeConverter {
    @TypeConverter
    fun toSensorType(value: String) = enumValueOf<SensorType>(value)

    @TypeConverter
    fun fromSensorType(value: SensorType) = value.name
}
