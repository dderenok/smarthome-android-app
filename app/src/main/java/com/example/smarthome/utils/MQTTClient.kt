package com.example.smarthome.utils

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.example.smarthome.database.SensorApplication
import com.example.smarthome.database.entity.Sensor
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.database.enumeration.SensorType.LIGHT
import com.example.smarthome.database.viewmodel.SensorViewModel
import com.example.smarthome.database.viewmodel.SensorViewModelFactory
import com.example.smarthome.model.MQTTSensorDto
import kotlinx.serialization.UnstableDefault
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlinx.serialization.json.Json
import java.util.*

class MQTTClient(
    private val viewModel: SensorViewModel
) {
    private lateinit var mqttClient: MqttAndroidClient

    @OptIn(UnstableDefault::class)
    fun connectToBroker(context: Context) {
        val serverURI = "tcp://10.0.2.2:1883"

        mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                val mqttSensorDto = Json.parse(MQTTSensorDto.serializer(), message.toString())
                viewModel.allSensors.value
                    ?.first { it.name == mqttSensorDto.name }
                    ?.apply { sensorValue = mqttSensorDto.value.toDouble() }
                    ?.let { viewModel.update(it) }
                    .also { Log.d(TAG, "Updated sensors from DB: $this") }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })

        val options = MqttConnectOptions()
        try {
            mqttClient.connect(
                options,
                null,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "Connection success")
                        subscribe("sensor/temperature")
                        subscribe("sensor/light")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "Connection failure: $exception")
                    }
                }
            )
        } catch (exception: MqttException) {
            Log.e(TAG, "Something went wrong: $exception")
        }
    }

    private fun subscribe(topic: String, qos: Int = 1) {
        try {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }
            })
        } catch (exception: MqttException) {
            Log.e(TAG, "Something went wrong: $exception")
        }
    }

    fun publish(
        topic: String,
        publishMessage: String,
        qos: Int = 1,
        retained: Boolean = false
    ) {
        try {
            val message = MqttMessage()
            message.payload = publishMessage.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$publishMessage published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $publishMessage to $topic")
                }
            })
        } catch (exception: MqttException) {
            Log.e(TAG, "Something went wrong: $exception")
        }
    }

    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (exception: MqttException) {
            Log.e(TAG, "Something went wrong: $exception")
        }
    }

    companion object {
        const val TAG = "AndroidMqttClient"
    }
}
