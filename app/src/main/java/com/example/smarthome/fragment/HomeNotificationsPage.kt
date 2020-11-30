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
import com.example.smarthome.`interface`.OnClickNotification
import com.example.smarthome.adapter.HomePageAdapter
import com.example.smarthome.model.UserNotification

class HomeNotificationsPage : Fragment(recyclerview_start_page_notification_item_row) {
    private lateinit var recyclerView: RecyclerView
    private var userNotificationList = listOf<UserNotification>()

    init {
        userNotificationList = userNotificationList + listOf(
            UserNotification("Object on video", "Unknown object near home"),
            UserNotification("Sensor issue", "Temperature_1 sensor doesn't respond."),
            UserNotification("Sensor issue", "Temperature_1 sensor doesn't respond."),
            UserNotification("Sensor issue", "HighLight sensor doesn't respond.")
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

        if (userNotificationList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

//        val removeNotificationButton = view.findViewById<Button>(R.id.remove_notification)
//
//        removeNotificationButton.setOnClickListener {
//            removeNotification(it)
//        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = HomePageAdapter(userNotificationList)
        }

        return view
    }

//    fun removeNotification(view: View) {
//        userNotificationList.drop(view.id)
//    }
//
//    override fun onNotificationClick(position: Int) {
//        TODO("Not yet implemented")
//    }
}
