package com.smhrd.stucamp.chat


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.smhrd.stucamp.VO.KakaoVO

// 클래스 옆 소괄호안 : 생성자
class KakaoAdapter(var context: Context, var template : Int, var data: ArrayList<KakaoVO>, var user_nickname: String?)
    : Adapter<KakaoViewHolder>(){
    // template -> xml -> int타입

    // 상위클래스인 Adapter 클래스가 추상클래스이기 때문이다.
    // => 추상클래스를 상속받는 하위클래스는 반드시 추상메소드를 오버라이딩(재정의) 해야한다!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KakaoViewHolder {
        // 한번만 실행됨 , onBindViewHolder 가 재활용함
        // public KakaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){}

        // ViewHolder를 생성하는곳
        
        // xml => kt(java) 객체로 만드는 작업을 Inflater 라고 부른다.
        // 대표적으로 findViewById
        // ViewHolder를 생성할 때 Template.xml을 View타입으로 변환해서 전달
        var template_View : View = LayoutInflater.from(context).inflate(template, parent, false)
        var VH : KakaoViewHolder = KakaoViewHolder(template_View)
        return VH
    }

    override fun getItemCount(): Int {
        // 전체 아이템의 개수를 리턴하는 곳!
        // return 5; =>
        return data.size // 전체 메시지의 개수
    }

    override fun onBindViewHolder(holder: KakaoViewHolder, position: Int) {
        // 이전에 쓰던 ViewHolder에서 View들 꺼내다가 ArrayList에 저장된 데이터들로 꾸미는 곳!
        var KakaoMessage : KakaoVO = data.get(position)

        // 상대방
        holder.tv_msg.text = KakaoMessage.msg
        holder.img.setImageResource(KakaoMessage.imgId!!)
        holder.tv_name.text = data.get(position).name
        holder.tv_time.text = data.get(position).time

        // 로그인한 사용자
        holder.tv_myMsg.text = KakaoMessage.msg
        holder.tv_myTime.text = data.get(position).time

        if(user_nickname == KakaoMessage.name){
            holder.tv_msg.visibility = GONE
            holder.img.visibility = GONE
            holder.tv_name.visibility = GONE
            holder.tv_time.visibility = GONE
            holder.cv_pic.visibility = GONE

        }else{
            holder.tv_myMsg.visibility = GONE
            holder.tv_myTime.visibility = GONE

        }





    }
}