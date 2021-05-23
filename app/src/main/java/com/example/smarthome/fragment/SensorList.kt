package com.example.smarthome.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater.from
import android.widget.ImageView
import android.widget.Toast.makeText
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R.id.add_sensor_image_button
import com.example.smarthome.R.id.delete_all_sensors
import com.example.smarthome.R.id.recyclerView as sensorRecyclerView
import com.example.smarthome.R.layout.fragment_sensor_list
import com.example.smarthome.R.menu.header_menu
import com.example.smarthome.`interface`.OnItemClickListener
import com.example.smarthome.activity.AddEditSensorActivity
import com.example.smarthome.adapter.SensorAdapter
import com.example.smarthome.database.SensorApplication
import com.example.smarthome.database.entity.Sensor
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.database.enumeration.SensorType.LIGHT
import com.example.smarthome.database.viewmodel.SensorViewModel
import com.example.smarthome.database.viewmodel.SensorViewModelFactory
import com.example.smarthome.model.SensorDto
import com.example.smarthome.model.SensorDto.Companion.fromEntity
import com.example.smarthome.utils.MQTTClient
import org.eclipse.paho.client.mqttv3.MqttClient

class SensorList : Fragment(fragment_sensor_list) {
    private lateinit var recyclerView: RecyclerView
    private var sensorList = mutableListOf<SensorDto>()
    private val viewModel: SensorViewModel by viewModels {
        SensorViewModelFactory((activity?.application as SensorApplication).repository)
    }
    private lateinit var mqttClient: MQTTClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mqttClient = MQTTClient(viewModel)
        mqttClient.connectToBroker(activity?.applicationContext!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_SENSOR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) saveIntentDataToBase(data)
        } else if (requestCode == EDIT_SENSOR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) updateIntentDataToBase(data)
        } else {
            makeText(
                activity?.applicationContext,
                "Sensor does not saved",
                LENGTH_LONG
            ).show()
        }
    }

    private fun saveIntentDataToBase(data: Intent) {
        if (data.extras!!["sensorName"] != null && data.extras!!["sensorRoomName"] != null) {
            val sensor = Sensor(
                name = data.extras!!["sensorName"].toString(),
                roomName = data.extras!!["sensorRoomName"].toString(),
                sensorType = enumValueOf<SensorType>(data.extras!!["sensorType"].toString()),
                sensorValue = 0.0
            )
            viewModel.insert(sensor = sensor)
            makeText(activity?.applicationContext, "Sensor saved", LENGTH_SHORT)
                .show()
        }
    }

    private fun updateIntentDataToBase(data: Intent) {
        val sensorId = data.getLongExtra("sensorId", -1)

        if (sensorId == -(1).toLong()) {
            makeText(activity?.applicationContext, "Sensor can't be updated", LENGTH_SHORT)
                .show()
            return
        }

        val sensorType = data.extras!!["sensorType"].toString()
        val sensorValue = setCorrectSensorValue(sensorType, data.extras!!["sensorValue"].toString())

        val sensor = Sensor(
            name = data.extras!!["sensorName"].toString(),
            roomName = data.extras!!["sensorRoomName"].toString(),
            sensorType = enumValueOf(sensorType),
            sensorValue = sensorValue
        )
        sensor.id = sensorId
        viewModel.update(sensor = sensor)
        sendCommandToSensor(sensorType, sensorValue)
        makeText(activity?.applicationContext, "Sensor updated", LENGTH_SHORT)
            .show()
    }

    // Is turn on (1.0) or turn off (0.0) in case light lump
    private fun setCorrectSensorValue(sensorType: String, sensorValue: String) =
        if (sensorType == "LIGHT") {
            if (sensorValue.toBoolean()) 1.0
            else 0.0
        }
        else sensorValue.toDouble()

    private fun sendCommandToSensor(sensorType: String, sensorValue: Double) {
        if (sensorType == "LIGHT") {
            mqttClient.publish("sensor/light", if (sensorValue == 1.0) "1" else "0")
        }
    }

    private fun ViewGroup?.inflate(layoutRes: Int, attachToRoot: Boolean = false) = from(context)
        .inflate(layoutRes, this, attachToRoot)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = container.inflate(fragment_sensor_list, false)
        val sensorAdapter = SensorAdapter(sensorList)
        val addSensorImageButton = view.findViewById<ImageView>(add_sensor_image_button)
        addSensorImageButton.setOnClickListener {
            val intent = Intent(activity?.applicationContext, AddEditSensorActivity::class.java)
            startActivityForResult(intent, ADD_SENSOR_ACTIVITY_REQUEST_CODE)
        }

        recyclerView = view.findViewById<RecyclerView>(sensorRecyclerView)
            .apply {
                setHasFixedSize(true)
                setHasOptionsMenu(true)
                layoutManager = LinearLayoutManager(view.context)
                adapter = sensorAdapter
            }

        viewModel.allSensors.observe(viewLifecycleOwner, Observer { sensorList ->
            sensorList?.let {
                val sensorDtoList = it.map { sensor ->
                    fromEntity(sensor)
                }.toMutableList()
                sensorAdapter.setSensors(sensorDtoList)
            }
        })

        openSensorEditIfExistArguments()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete(sensorAdapter.getSensorByPosition(viewHolder.adapterPosition))
                makeText(activity?.applicationContext, "Sensor deleted", LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        sensorAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(sensor: Sensor) {
                val intent = Intent(activity?.applicationContext, AddEditSensorActivity::class.java)
                intent.putExtra("sensorId", sensor.id)
                intent.putExtra("sensorName", sensor.name)
                intent.putExtra("sensorRoomName", sensor.roomName)
                intent.putExtra("sensorType", sensor.sensorType.name)
                intent.putExtra("sensorValue", getCorrectSensorValue(sensor.sensorType, sensor.sensorValue))
                startActivityForResult(intent, EDIT_SENSOR_ACTIVITY_REQUEST_CODE)
            }
        })

        return view
    }

    private fun getCorrectSensorValue(sensorType: SensorType, sensorValue: Double) =
        if (sensorType == LIGHT) (sensorValue != 0.0).toString()
        else sensorValue.toString()

    private fun openSensorEditIfExistArguments() {
        if (arguments?.getString("sensorId") != null &&
            arguments?.getString("sensorName") != null &&
            arguments?.getString("sensorRoomName") != null &&
            arguments?.getString("sensorValue") != null &&
            arguments?.getString("sensorType") != null)
        {
            val intent = Intent(activity?.applicationContext, AddEditSensorActivity::class.java)
            intent.putExtra("sensorId", arguments?.getString("sensorId")?.toLong())
            intent.putExtra("sensorName", arguments?.getString("sensorName"))
            intent.putExtra("sensorRoomName", arguments?.getString("sensorRoomName"))
            intent.putExtra("sensorType", arguments?.getString("sensorType"))
            intent.putExtra("sensorValue", arguments?.getString("sensorValue"))

            startActivityForResult(intent, EDIT_SENSOR_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(header_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        delete_all_sensors -> {
            viewModel.deleteAll()
            makeText(activity?.applicationContext, "All sensors deleted", LENGTH_SHORT).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ADD_SENSOR_ACTIVITY_REQUEST_CODE = 1
        private const val EDIT_SENSOR_ACTIVITY_REQUEST_CODE = 2
    }
}
