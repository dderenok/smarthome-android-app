package com.example.smarthome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R.id.recyclerView as recyclerViewLayout
import com.example.smarthome.R.layout.fragment_room_list
import com.example.smarthome.adapter.RoomAdapter
import com.example.smarthome.model.RoomDto
import org.eclipse.paho.client.mqttv3.MqttClient

class RoomList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var roomList = listOf<RoomDto>()

    init {
        roomList = roomList + listOf(RoomDto("Kitchen", 1))
        roomList = roomList + listOf(RoomDto("Basement", 1))
        roomList = roomList + listOf(RoomDto("Hall", 2))
        roomList = roomList + listOf(RoomDto("Garage", 1))
    }

    private fun ViewGroup?.inflate(layoutRes: Int, attachToRoot: Boolean = false) = from(context)
        .inflate(layoutRes, this, attachToRoot)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = container.inflate(fragment_room_list, false)

        if (roomList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

        recyclerView = view.findViewById<RecyclerView>(recyclerViewLayout).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = RoomAdapter(roomList)
        }

        return view
    }
}
