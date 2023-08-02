package com.smhrd.stucamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smhrd.stucamp.VO.KakaoVO

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

        val database = Firebase.database
        val myRef = database.getReference("message")

        var data = ArrayList<KakaoVO>()

    }
}