package com.example.smarthome.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smarthome.R.id.bottomNavigationView
import com.example.smarthome.R.id.flFragment
import com.example.smarthome.R.id.home_page
import com.example.smarthome.R.id.sensor_list
import com.example.smarthome.R.id.sensor_scanning
import com.example.smarthome.R.id.room_list
import com.example.smarthome.R.layout.activity_main
import com.example.smarthome.fragment.HomePage
import com.example.smarthome.fragment.RoomList
import com.example.smarthome.fragment.SensorList
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setNavigationMenuSettings()
    }

    private fun setNavigationMenuSettings() {
        val currentFragment = defineCurrentFragmentToView()

        setCurrentFragment(currentFragment)

        findViewById<BottomNavigationView>(bottomNavigationView)
            .setNavigationItemListener(currentNavigationItem = currentFragment)
    }

    private fun defineCurrentFragmentToView(): Fragment {
        var fragmentToStart: String?
        var currentFragment: Fragment? = null

        if (intent.extras?.get("fragmentToStart") != null) {
            fragmentToStart = intent.extras?.get("fragmentToStart").toString()
            currentFragment = when (fragmentToStart) {
                "SensorList" -> {
                    val sensorListFragment = SensorList()
                    val bundle = Bundle()
                    if (intent.extras?.get("sensorId") != null &&
                        intent.extras?.get("sensorName") != null &&
                        intent.extras?.get("sensorRoomName") != null)  {
                        bundle.putString("sensorId", intent.extras?.get("sensorId").toString())
                        bundle.putString("sensorName", intent.extras?.get("sensorName").toString())
                        bundle.putString("sensorRoomName", intent.extras?.get("sensorRoomName").toString())
                    }
                    sensorListFragment.arguments = bundle
                    return sensorListFragment
                }
                "RoomList" -> RoomList()
                "HomePage" -> HomePage()
                else -> null
            }
        }

        return currentFragment ?: SensorList()
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction()
        .apply {
            replace(flFragment, fragment)
            commit()
        }

    private fun BottomNavigationView.setNavigationItemListener(currentNavigationItem: Fragment) =
        setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                sensor_list -> setCurrentFragment(currentNavigationItem)
                room_list -> setCurrentFragment(RoomList())
                home_page -> setCurrentFragment(HomePage())
                sensor_scanning -> {
                    val intent = Intent(this@MainActivity, ScannerActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
}
