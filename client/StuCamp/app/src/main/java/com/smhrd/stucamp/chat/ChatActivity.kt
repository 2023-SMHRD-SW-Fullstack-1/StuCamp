package com.smhrd.stucamp.chat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.smhrd.stucamp.R
import com.smhrd.stucamp.VO.KakaoVO
import com.smhrd.stucamp.VO.UserVO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChatActivity : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var btn_send : Button
    lateinit var edt_msg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rv = findViewById(R.id.rv_result)
        btn_send = findViewById(R.id.btn_send)
        edt_msg = findViewById(R.id.edt_msg)

        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", " "), UserVO::class.java)



        val it = getIntent()
        Log.d("intentTest", it.getIntExtra("room_id", 0).toString())

        val database = Firebase.database
        val myRef = database.getReference("message")

        val roomRef = database.getReference("roomList")
        val idRef = roomRef.child(it.getIntExtra("room_id", 0).toString())
        val chatsRef = idRef.child("chat")
//        chatsRef.push().setValue(KakaoVO(R.drawable.img1, "김혁3", "안녕하세요^^", "오후 4:17"))


        var data = ArrayList<KakaoVO>()

        fun getCurrentTimeInHHMMFormat(): String {
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeZone = TimeZone.getTimeZone("GMT+9")
            dateFormat.timeZone = timeZone

            return dateFormat.format(Date(currentTime))
        }

        var adapter: KakaoAdapter = KakaoAdapter(applicationContext, R.layout.template, data)

        // layoutManager 세팅
        rv.layoutManager = LinearLayoutManager(applicationContext)
        rv.adapter = adapter

        btn_send.setOnClickListener {
            chatsRef.push().setValue(KakaoVO(R.drawable.img1, user.user_nickname, edt_msg.text.toString(), getCurrentTimeInHHMMFormat()))
            // adapter 새로고침
            adapter.notifyDataSetChanged()
            // 스크롤 옮기기
            if(data.size >= 1){
                rv.smoothScrollToPosition(data.size - 1)
            }
            // EditText 비우기
            edt_msg.text.clear()

        }

        idRef.child("chat").addChildEventListener(ChildEvent(data, adapter))

    }
}