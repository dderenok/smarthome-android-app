package com.example.smarthome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.R.layout.recyclerview_start_page_notification_item_row
import com.example.smarthome.adapter.HomePageAdapter
import com.example.smarthome.model.UserNotificationDto

class HomeNotificationsPage : Fragment(recyclerview_start_page_notification_item_row) {
    private lateinit var recyclerView: RecyclerView
    private var userNotificationList = listOf<UserNotificationDto>()

    init {
        userNotificationList = userNotificationList + listOf(
            UserNotificationDto("Sensor issue", "Temperature_1 sensor doesn't respond."),
            UserNotificationDto("Sensor issue", "Temperature_2 sensor doesn't respond."),
            UserNotificationDto("Sensor issue", "HighLight sensor doesn't respond.")
        )
    }

    private fun ViewGroup?.inflate(layoutRes: Int, attachToRoot: Boolean = false) = from(context)
        .inflate(layoutRes, this, attachToRoot)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = container.inflate(recyclerview_start_page_notification_item_row, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = HomePageAdapter(userNotificationList)
        }

        return view
    }
}
