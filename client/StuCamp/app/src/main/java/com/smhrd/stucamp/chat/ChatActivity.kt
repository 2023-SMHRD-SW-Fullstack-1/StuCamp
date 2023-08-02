package com.smhrd.stucamp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smhrd.stucamp.R
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

        // add 함수 사용하여 메시지 5개 저장~
        myRef.push().setValue(KakaoVO(R.drawable.img1, "김혁", "안녕하세요^^", "오후 4:17"))



        // 내가 보낸 채팅 오른쪽에 띄우는법 !
        // 1. template.xml 파일에 오른쪽 톡 추가! (중요! 같은 파일에 추가 할 것!)
        // 2. 현재 로그인한 ID를 adapter 생성자로 전송 => 메시지 주인
        // 3. adapter 클래스의 onBindView메소드에서 data.get(position).name(메시지주인)과 생성자로
        // 전달된 id를 비교
        // 4. 일치한다면 왼쪽 뷰들은 전부 gone, 오른쪽 뷰들은 visible
        //   template에 뷰가 추가됐으니 ViewHolder도 수정 필요!!!
        var adapter: KakaoAdapter = KakaoAdapter(applicationContext, R.layout.template, data)

        // layoutManager 세팅
        rv.layoutManager = LinearLayoutManager(applicationContext)
        rv.adapter = adapter

        btn_send.setOnClickListener {
            myRef.push().setValue(KakaoVO(R.drawable.img1, "펑이", edt_msg.text.toString(), "오후 16:38"))
            // adapter 새로고침
            adapter.notifyDataSetChanged()
            // 스크롤 옮기기
            rv.smoothScrollToPosition(data.size - 1)
            // EditText 비우기
            edt_msg.text.clear()

        }

        myRef.addChildEventListener(ChildEvent(data, adapter))

    }
}