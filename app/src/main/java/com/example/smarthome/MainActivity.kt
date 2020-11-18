package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R.id.recyclerView as recyclerViewLayout
import com.example.smarthome.R.layout.activity_main
import com.example.smarthome.adapter.RoomAdapter
import com.example.smarthome.model.Room

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

        if (roomList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

        performRecyclerView()
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
