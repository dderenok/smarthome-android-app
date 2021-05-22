package com.example.smarthome.model

import kotlinx.serialization.Serializable

@Serializable
class MQTTSensorDto(
    val value: String,
    val name: String
)
