package com.example.smarthome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.R
import com.example.smarthome.R.id.first_name
import com.example.smarthome.R.id.second_name
import com.example.smarthome.R.layout.fragment_home_page
import com.example.smarthome.adapter.HomePageAdapter
import com.example.smarthome.model.UserNotificationDto

class HomePage : Fragment(fragment_home_page) {
    private lateinit var recyclerView: RecyclerView
    private var userNotificationList = listOf<UserNotificationDto>()

    init {
        userNotificationList = userNotificationList + listOf(
                UserNotificationDto("Object on video", "Unknown object near home"),
                UserNotificationDto("Sensor issue", "Temperature_1 sensor doesn't respond."),
                UserNotificationDto("Sensor issue", "Temperature_1 sensor doesn't respond."),
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
        val view = container.inflate(fragment_home_page, false)

        if (userNotificationList.isEmpty()) {
            // TODO d.derenok: Add generate room list method
        }

//        val removeNotificationButton = view.findViewById<Button>(R.id.remove_notification)
//
//        removeNotificationButton.setOnClickListener {
//            removeNotification(it)
//        }

        val firstName = view.findViewById<TextView>(first_name)
        val secondName = view.findViewById<TextView>(second_name)
        firstName.text = "FirstName"
        secondName.text = "SecondName"

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
}
