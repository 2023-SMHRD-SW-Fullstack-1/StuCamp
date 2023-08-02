package com.smhrd.stucamp.chatlist

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.smhrd.stucamp.VO.ChatListVO
import com.smhrd.stucamp.chat.KakaoAdapter

class ChatListChildEvent(var data: ArrayList<ChatListVO>, var adapter: ChatListAdapter) :
    ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        var temp: ChatListVO? = snapshot.getValue(ChatListVO::class.java)
        data.add(temp!!)
        adapter.notifyDataSetChanged()
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {

    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

}