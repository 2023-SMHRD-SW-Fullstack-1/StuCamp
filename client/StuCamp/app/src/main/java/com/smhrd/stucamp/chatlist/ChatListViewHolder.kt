package com.smhrd.stucamp.chatlist

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smhrd.stucamp.R

class ChatListViewHolder(var itemView : View) : ViewHolder(itemView) {

    var tv_roomTitle: TextView
    var btn_roomEnter: Button

    init {
        tv_roomTitle = itemView.findViewById(R.id.tv_roomTitle)
        btn_roomEnter = itemView.findViewById(R.id.btn_roomEnter)
    }
}