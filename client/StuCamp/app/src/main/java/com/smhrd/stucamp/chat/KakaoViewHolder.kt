package com.smhrd.stucamp.chat


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smhrd.stucamp.R

class KakaoViewHolder(var itemView : View) : ViewHolder(itemView){
    // 위에 괄호 -> 생성자
    // : 상속
    // 자바에서는
    // class KakaoViewHolder extends ViewHolder{
    //     KakaoViewHolder(View itemView){
    //     super(itemView)
    //     }
    // }

    var img : ImageView
    var tv_name : TextView
    var tv_msg : TextView
    var tv_time : TextView
    
    // default 생성자 => 매개변수가 하나도 없는 생성자!
    init{
        img = itemView.findViewById(R.id.img)
        tv_msg = itemView.findViewById(R.id.tv_msg)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_time = itemView.findViewById(R.id.tv_time)
    }






}