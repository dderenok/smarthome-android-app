package com.example.smarthome.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smarthome.R.id.notification_type
import com.example.smarthome.R.id.notification_message
import com.example.smarthome.`interface`.OnClickNotification

class HomePageViewHolder(view: View) : ViewHolder(view) {
    var notificationType: TextView = view.findViewById(notification_type)
    var notificationMessage: TextView = view.findViewById(notification_message)
    lateinit var onNotificationListener: OnClickNotification

//    constructor(view: View, onClickNotification: OnClickNotification) {
//        this.onNotificationListener = onClickNotification
//        notificationRemoveButton.setOnClickListener(this)
//    }

//    override fun onClick(view: View?) {
//        onNotificationListener.onNotificationClick(adapterPosition)
//    }
}
