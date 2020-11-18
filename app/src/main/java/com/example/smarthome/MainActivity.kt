package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R.id.flFragment
import com.example.smarthome.R.id.recyclerView as recyclerViewLayout
import com.example.smarthome.R.layout.activity_main
import com.example.smarthome.adapter.RoomAdapter
import com.example.smarthome.fragment.HomePage
import com.example.smarthome.fragment.RoomList
import com.example.smarthome.fragment.SensorList
import com.example.smarthome.model.Room
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var roomList = listOf<Room>()

    init {
        roomList = roomList + listOf(Room("Кухня", 3))
        roomList = roomList + listOf(Room("Ванная", 1))
        roomList = roomList + listOf(Room("Подвал", 2))
        roomList = roomList + listOf(Room("Ванная", 1))
        roomList = roomList + listOf(Room("Подвал", 2))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        d(MAIN_LOG_TAG, "Main page started")

        val sensorListFragment = SensorList()
        val homePageFragment = HomePage()
        val roomListFragment = RoomList()

        setCurrentFragment(sensorListFragment)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.sensor_list->setCurrentFragment(sensorListFragment)
                R.id.home_page->setCurrentFragment(homePageFragment)
                R.id.room_list->setCurrentFragment(roomListFragment)
            }
            true
        }

        if (roomList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

        performRecyclerView()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(flFragment, fragment)
            commit()
        }

    private fun performRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = RoomAdapter(roomList)

        recyclerView = findViewById<RecyclerView>(recyclerViewLayout).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        private var MAIN_LOG_TAG = MainActivity::class.java.simpleName
    }
}
