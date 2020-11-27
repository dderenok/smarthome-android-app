package com.example.smarthome.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smarthome.R.id.room_name_info
import com.example.smarthome.R.id.sensor_name_info

class SensorViewHolder(view: View) : ViewHolder(view) {
    var roomName: TextView = view.findViewById(room_name_info)
    var sensorName: TextView = view.findViewById(sensor_name_info)
}
