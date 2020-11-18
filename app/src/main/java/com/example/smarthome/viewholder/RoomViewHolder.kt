package com.example.smarthome.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smarthome.R.id.amount_of_sensors
import com.example.smarthome.R.id.info_text

class RoomViewHolder(view: View) : ViewHolder(view) {
    var infoText: TextView = view.findViewById(info_text)
    var amountOfSensors: TextView = view.findViewById(amount_of_sensors)
}
