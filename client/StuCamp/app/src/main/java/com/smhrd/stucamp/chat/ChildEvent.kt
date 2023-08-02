package com.smhrd.stucamp.chat

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.smhrd.stucamp.VO.KakaoVO

class ChildEvent(var data: ArrayList<KakaoVO>, var adapter: KakaoAdapter, var rv: RecyclerView, var size: Int) : ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        // 데이터 추가 감지
        // snapshot => firebase database 에 저장된 데이터
        // jason구조로 응답함 => KakaoVO 형태로 변환
        var temp: KakaoVO? = snapshot.getValue(KakaoVO::class.java)
        data.add(temp!!) // temp 가 null 일수도 있음을 표기
        if(data.size >= 1){
            rv.smoothScrollToPosition(data.size - 1)
        }
        adapter.notifyDataSetChanged()

    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        // 데이터 변경 감지
        // ArrayList에 추가된 데이터 추가하고 Adapter 새로고침
        if(data.size >= 1){
            rv.smoothScrollToPosition(data.size - 1)
        }
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        // 제거
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        // 옮겨짐
    }

    override fun onCancelled(error: DatabaseError) {
        // 뭔가 문제가 발생한걸 감지!
    }
}