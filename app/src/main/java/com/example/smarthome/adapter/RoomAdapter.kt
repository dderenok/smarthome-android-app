package com.example.smarthome.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smarthome.R.layout.recyclerview_item_row
import com.example.smarthome.model.Room
import com.example.smarthome.viewholder.RoomViewHolder

class RoomAdapter(private var roomList: List<Room>) : Adapter<RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RoomViewHolder(from(parent.context)
            .inflate(recyclerview_item_row, parent, false))

    private fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false) = from(context)
        .inflate(layoutRes, this, attachToRoot)

    override fun getItemCount() = roomList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.infoText.text = roomList[position].name
        holder.amountOfSensors.text = "${roomList[position].amountOfSensors} sensors"
    }
}
