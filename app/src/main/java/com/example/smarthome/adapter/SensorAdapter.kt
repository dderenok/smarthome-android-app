package com.example.smarthome.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smarthome.R.layout.recyclerview_sensor_item_row
import com.example.smarthome.model.Sensor
import com.example.smarthome.viewholder.SensorViewHolder

class SensorAdapter(private var sensorList: List<Sensor>) : Adapter<SensorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SensorViewHolder(
            from(parent.context)
            .inflate(recyclerview_sensor_item_row, parent, false)
        )

    override fun getItemCount() = sensorList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        holder.roomName.text = sensorList[position].room.name
        holder.sensorName.text = sensorList[position].name
    }
}
