package com.smhrd.stucamp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smhrd.stucamp.VO.ChatListVO
import com.smhrd.stucamp.chatlist.ChatListAdapter
import com.smhrd.stucamp.chatlist.ChatListChildEvent
import com.smhrd.stucamp.chatlist.ChatListCreate

class Fragment4 : Fragment() {

    lateinit var rv: RecyclerView
    lateinit var btn_createRoom : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.activity_chat_list, container, false)

        btn_createRoom = view.findViewById(R.id.btn_createRoom)
        rv = view.findViewById(R.id.rcChatList)

        val database = Firebase.database
        val roomRef = database.getReference("roomList")

        var data = ArrayList<ChatListVO>()
        var adapter = ChatListAdapter(requireContext(), R.layout.chatlist_item, data)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // 방 생성 버튼
        btn_createRoom.setOnClickListener {
            var intent = Intent(requireContext(), ChatListCreate::class.java)
            startActivity(intent)
        }

        roomRef.addChildEventListener(ChatListChildEvent(data,adapter))




        return view
    }

}