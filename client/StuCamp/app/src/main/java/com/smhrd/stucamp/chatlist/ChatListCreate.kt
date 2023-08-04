package com.smhrd.stucamp.chatlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.smhrd.stucamp.Fragment4
import com.smhrd.stucamp.MainActivity
import com.smhrd.stucamp.R
import com.smhrd.stucamp.VO.ChatListVO
import com.smhrd.stucamp.VO.KakaoVO
import com.smhrd.stucamp.VO.UserVO


class ChatListCreate : AppCompatActivity() {

    lateinit var edt_createRoomTitle: EditText
    lateinit var btn_makeRoom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list_create)

        edt_createRoomTitle = findViewById(R.id.edt_createRoomTitle)
        btn_makeRoom = findViewById(R.id.btn_makeRoom)

        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", " "), UserVO::class.java)

        val database = Firebase.database
        val roomRef = database.getReference("roomList")

        var query: Query = roomRef.orderByKey()

        btn_makeRoom.setOnClickListener {

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // 데이터 스냅샷에서 데이터 개수 파악
                    val dataCount = dataSnapshot.childrenCount

                    Log.d("Firebase 데이터 개수", dataCount.toString())
                    Log.d("User spf 데이터", user.user_email.toString())
//                    roomRef.push().setValue(ChatListVO(dataCount.toInt(),user.user_email ,edt_createRoomTitle.text.toString()))
                    val idRef = roomRef.child(dataCount.toString())
                    idRef.setValue(ChatListVO(dataCount.toInt(),user.user_email ,edt_createRoomTitle.text.toString()))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 읽기 작업이 취소되었거나 실패한 경우
                    Log.w("Firebase 에러", "데이터 개수 가져오기 취소됨", databaseError.toException())

                }

            })
            finish()

        }



    }
}