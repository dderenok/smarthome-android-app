package com.example.smarthome.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.example.smarthome.R
import com.example.smarthome.R.id.edit_sensor_name
import com.example.smarthome.R.id.edit_sensor_room_name
import com.example.smarthome.R.id.button_save
import com.example.smarthome.R.layout.activity_add_sensor

class AddEditSensorActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editRoomName: EditText
    private lateinit var saveButton: Button
    private lateinit var lightButtonStateLayout: View
    private lateinit var switchLightButton: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_add_sensor)

        editName = findViewById(edit_sensor_name)
        editRoomName = findViewById(edit_sensor_room_name)
        lightButtonStateLayout = findViewById<View>(R.id.light_switch_layout)
        switchLightButton = findViewById(R.id.light_switch)
        val isLightSensor = intent.getStringExtra("sensorType")
        if (isLightSensor == "LIGHT") {
            lightButtonStateLayout.visibility = VISIBLE
        } else {
            lightButtonStateLayout.visibility = INVISIBLE
        }
        saveButton = findViewById(button_save)

        saveButton.setListenerOnSaveButton()

        updateTitlePageByState()
    }

    private fun Button.setListenerOnSaveButton() = setOnClickListener {
        val replyIntent = Intent()
        if (isEmpty(editName.text) || isEmpty(editRoomName.text)) {
            setResult(RESULT_CANCELED, replyIntent)
        } else {
            replyIntent.putExtra(SENSOR_NAME_EXTRA_FIELD, editName.text.toString())
            replyIntent.putExtra(SENSOR_ROOM_NAME_EXTRA_FIELD, editRoomName.text.toString())
            val sensorType = intent.getStringExtra("sensorType")

            if (sensorType == "LIGHT")
                replyIntent.putExtra(SENSOR_VALUE_EXTRA_FIELD, switchLightButton.isChecked.toString())
            else replyIntent.putExtra(SENSOR_VALUE_EXTRA_FIELD, intent.getStringExtra("sensorValue"))
            replyIntent.putExtra(SENSOR_TYPE_EXTRA_FIELD, intent.getStringExtra("sensorType"))

            val sensorId = intent.getLongExtra(SENSOR_ID_EXTRA_FIELD, DEFAULT_EXTRA_LONG_VALUE)
            if (sensorId != DEFAULT_EXTRA_LONG_VALUE) {
                replyIntent.putExtra(SENSOR_ID_EXTRA_FIELD, sensorId)
            }
            setResult(RESULT_OK, replyIntent)
        }
        finish()
    }

    private fun updateTitlePageByState() {
        val intent = intent

        if (intent.hasExtra(SENSOR_ID_EXTRA_FIELD)) {
            title = EDIT_SENSOR_TITLE
            editName.setText(intent.getStringExtra(SENSOR_NAME_EXTRA_FIELD))
            editRoomName.setText(intent.getStringExtra(SENSOR_ROOM_NAME_EXTRA_FIELD))
            switchLightButton.isChecked =
                intent.getStringExtra(SENSOR_VALUE_EXTRA_FIELD)
                    ?.toBoolean()
                    ?: false
        } else {
            title = ADD_SENSOR_TITLE
        }
    }

    companion object {
        private const val ADD_SENSOR_TITLE = "Add Sensor"
        private const val EDIT_SENSOR_TITLE = "Edit Sensor"
        private const val SENSOR_ID_EXTRA_FIELD = "sensorId"
        private const val SENSOR_NAME_EXTRA_FIELD = "sensorName"
        private const val SENSOR_ROOM_NAME_EXTRA_FIELD = "sensorRoomName"
        private const val SENSOR_VALUE_EXTRA_FIELD = "sensorValue"
        private const val SENSOR_TYPE_EXTRA_FIELD = "sensorType"
        private const val DEFAULT_EXTRA_LONG_VALUE = -(1).toLong()
    }
}
