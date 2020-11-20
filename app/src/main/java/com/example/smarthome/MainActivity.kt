package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smarthome.R.id.bottomNavigationView
import com.example.smarthome.R.id.flFragment
import com.example.smarthome.R.id.home_page
import com.example.smarthome.R.id.sensor_list
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
        val roomListFragment = RoomList()
        setCurrentFragment(roomListFragment)

        findViewById<BottomNavigationView>(bottomNavigationView)
            .let {
                it.setNavigationItemListener(currentNavigationItem = roomListFragment)
            }
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction()
        .apply {
            replace(flFragment, fragment)
            commit()
        }

    private fun BottomNavigationView.setNavigationItemListener(currentNavigationItem: Fragment) =
        setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                room_list -> setCurrentFragment(currentNavigationItem)
                sensor_list -> setCurrentFragment(SensorList())
                home_page ->setCurrentFragment(HomePage())
            }
            true
        }
}
