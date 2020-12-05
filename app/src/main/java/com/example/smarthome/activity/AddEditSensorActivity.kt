package com.example.smarthome.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.Button
import android.widget.EditText
import com.example.smarthome.R.id.edit_sensor_name
import com.example.smarthome.R.id.edit_sensor_room_name
import com.example.smarthome.R.id.button_save
import com.example.smarthome.R.layout.activity_add_sensor

class AddEditSensorActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editRoomName: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_add_sensor)

        editName = findViewById(edit_sensor_name)
        editRoomName = findViewById(edit_sensor_room_name)
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
        private const val DEFAULT_EXTRA_LONG_VALUE = -(1).toLong()
    }
}
