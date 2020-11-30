package com.example.smarthome.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smarthome.R.layout.recyclerview_start_page_notification_item_row
import com.example.smarthome.model.UserNotification
import com.example.smarthome.viewholder.HomePageViewHolder

class HomePageAdapter(
    private var userNotificationList: List<UserNotification>
) : Adapter<HomePageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HomePageViewHolder(
            from(parent.context)
                    .inflate(recyclerview_start_page_notification_item_row, parent, false))

    override fun getItemCount() = userNotificationList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.notificationType.text = userNotificationList[position].type
        holder.notificationMessage.text = userNotificationList[position].message
    }
}
