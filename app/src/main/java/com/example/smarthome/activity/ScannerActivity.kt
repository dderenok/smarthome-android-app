package com.example.smarthome.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.util.Log.d as debugLog
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import com.example.smarthome.R.layout.activity_scanner
import com.example.smarthome.database.enumeration.SensorType
import com.example.smarthome.database.enumeration.SensorType.LIGHT
import com.example.smarthome.model.RoomDto
import com.example.smarthome.model.SensorDto
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.ALL_CODE_TYPES
import com.google.zxing.integration.android.IntentIntegrator.parseActivityResult
import com.google.zxing.integration.android.IntentResult
import org.json.JSONObject

class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_scanner)

        scanCode()
    }

    private fun scanCode() = IntentIntegrator(this)
        .apply {
            captureActivity = CaptureTempActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(ALL_CODE_TYPES)
            setPrompt("Scanning Code")
            setTimeout(TIMEOUT_SCANNING_VALUE)
            initiateScan()
        }

    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val sensor = getSensorFromScanResult(result.contents)
                builder.setMessage("Sensor identifier - ${sensor.id}, sensor name - ${sensor.name}," +
                        " sensor room - ${sensor.room.name}")
                builder.setTitle("Scanning Result")
                builder
                    .setPositiveButton("Scan Again") { _, _ -> scanCode() }
                    .setNegativeButton("Finish") { _, _ -> scanFinish(sensorToOpen = null) }
                    .setNeutralButton("Edit sensor info")  { _, _ -> backToMainActivity(sensor) }

                builder
                    .create()
                    .show()
            } else {
                backToMainActivity(sensorToOpen = null)
                showNoResultView()
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getSensorFromScanResult(scanResultContent: String) = SensorDto(
        id = scanResultContent.getJsonFieldByName("id").toLong(),
        name = scanResultContent.getJsonFieldByName("name"),
        room = scanResultContent.getRoomObject(),
        sensorValue = 0.0,
        sensorType = LIGHT
    )

    private fun String.getJsonFieldByName(fieldName: String) =
        JSONObject(this)[fieldName]
            .toString()

    private fun String.getRoomObject() =
        RoomDto(name = this.getJsonFieldByName("room"), amountOfSensors = 0)

    private fun scanFinish(sensorToOpen: SensorDto?) {
        debugLog(this::class.java.simpleName, "Got sensor: $sensorToOpen")
        backToMainActivity(sensorToOpen)
    }

    private fun backToMainActivity(sensorToOpen: SensorDto?) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("fragmentToStart", "SensorList")
        if (sensorToOpen != null) {
            bundle.putString("sensorId", sensorToOpen.id.toString())
            bundle.putString("sensorName", sensorToOpen.name)
            bundle.putString("sensorRoomName", sensorToOpen.room.name)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun showNoResultView() = makeText(this, NO_RESULTS, LENGTH_LONG)
        .apply {
            setGravity(BOTTOM or CENTER, 0, 210)
        }
        .show()

    companion object {
        private const val NO_RESULTS = "No Results"
        private const val TIMEOUT_SCANNING_VALUE = 30000.toLong()
    }
}
