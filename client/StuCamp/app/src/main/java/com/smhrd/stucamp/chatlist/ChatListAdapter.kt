package com.smhrd.stucamp.chatlist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.smhrd.stucamp.VO.ChatListVO
import com.smhrd.stucamp.VO.KakaoVO
import com.smhrd.stucamp.chat.ChatActivity
import com.smhrd.stucamp.chat.KakaoViewHolder

class ChatListAdapter (var context: Context, var template : Int, var data: ArrayList<ChatListVO>)
    : Adapter<ChatListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        var template_View : View = LayoutInflater.from(context).inflate(template, parent, false)
        var VH = ChatListViewHolder(template_View)
        return VH
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        var tv_roomTitle: TextView = holder.tv_roomTitle
        var btn_roomEnter: Button = holder.btn_roomEnter

        var roomVO : ChatListVO = data.get(position)

        tv_roomTitle.text = roomVO.title
        btn_roomEnter.setOnClickListener {

            val it = Intent(context, ChatActivity::class.java)
            it.putExtra("room_id", roomVO.id)
            context.startActivity(it)

        }

    }

}