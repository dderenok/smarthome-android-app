package com.example.smarthome.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.smarthome.R
import com.example.smarthome.R.id.sensor_value_info
import com.example.smarthome.R.layout.recyclerview_sensor_item_row
import com.example.smarthome.`interface`.OnItemClickListener
import com.example.smarthome.database.entity.Sensor.Companion.toEntity
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.database.enumeration.SensorType.LIGHT
import com.example.smarthome.model.SensorDto
import com.example.smarthome.utils.MQTTClient
import org.eclipse.paho.client.mqttv3.MqttClient

class SensorAdapter(
    private var sensorList: MutableList<SensorDto>
) : Adapter<SensorAdapter.SensorViewHolder>() {
    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        return SensorViewHolder(
            from(parent.context)
                .inflate(recyclerview_sensor_item_row, parent, false)
        ).listen { position, _ ->
            if (position != NO_POSITION) {
                listener.onItemClick(toEntity(sensorList[position]))
            }
        }
    }

    override fun getItemCount() = sensorList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        holder.roomName.text = sensorList[position].room.name
        holder.sensorName.text = sensorList[position].name
        holder.sensorValue.text = determineSensorTypeForStatusMessage(sensorList[position])
    }

    private fun determineSensorTypeForStatusMessage(sensorDto: SensorDto) =
        if (sensorDto.sensorType == LIGHT)
            "Light status is: ${getLightCorrectValue(sensorDto.sensorValue)}"
        else "Temperature value is: ${sensorDto.sensorValue} ℃"

    private fun getLightCorrectValue(sensorValue: Double) =
        if (sensorValue == 0.0) "OFF"
        else "ON"

    @SuppressLint("NotifyDataSetChanged")
    fun setSensors(sensorList: MutableList<SensorDto>) {
        this.sensorList = sensorList
        notifyDataSetChanged()
    }

    fun getSensorByPosition(position: Int) = toEntity(sensorList[position])

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var roomName: TextView = view.findViewById(R.id.room_name_info)
        var sensorName: TextView = view.findViewById(R.id.sensor_name_info)
        var sensorEditIcon: ImageView = view.findViewById(R.id.sensor_edit_icon)
        var sensorValue: TextView = view.findViewById(sensor_value_info)
    }

}
