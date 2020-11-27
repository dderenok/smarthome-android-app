package com.example.smarthome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R.id.recyclerView as sensorRecyclerView
import com.example.smarthome.R.layout.fragment_sensor_list
import com.example.smarthome.adapter.SensorAdapter
import com.example.smarthome.model.Room
import com.example.smarthome.model.Sensor

class SensorList : Fragment(fragment_sensor_list) {
    private lateinit var recyclerView: RecyclerView
    private var sensorList = listOf<Sensor>()

    init {
        sensorList = sensorList + listOf(Sensor("HighLight", Room("Kitchen", 2)))
        sensorList = sensorList + listOf(Sensor("Temperature_1", Room("Bathroom", 1)))
        sensorList = sensorList + listOf(Sensor("Temperature_2", Room("Kitchen", 2)))
        sensorList = sensorList + listOf(Sensor("Light_1", Room("Hall", 2)))
        sensorList = sensorList + listOf(Sensor("Temperature_3", Room("Hall", 2)))
    }

    private fun ViewGroup?.inflate(layoutRes: Int, attachToRoot: Boolean = false) = from(context)
        .inflate(layoutRes, this, attachToRoot)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = container.inflate(fragment_sensor_list, false)

        if (sensorList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

        recyclerView = view.findViewById<RecyclerView>(sensorRecyclerView)
            .apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(view.context)
                adapter = SensorAdapter(sensorList)
            }

        return view
    }
}
