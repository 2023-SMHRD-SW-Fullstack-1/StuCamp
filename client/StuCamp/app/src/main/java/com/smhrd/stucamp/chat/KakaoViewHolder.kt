package com.smhrd.stucamp.chat


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smhrd.stucamp.R
import org.w3c.dom.Text

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
    var tv_myMsg: TextView
    var tv_myTime: TextView
    var cv_pic: CardView
    
    // default 생성자 => 매개변수가 하나도 없는 생성자!
    init{
        img = itemView.findViewById(R.id.img)
        tv_msg = itemView.findViewById(R.id.tv_msg)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_time = itemView.findViewById(R.id.tv_time)
        tv_myMsg = itemView.findViewById(R.id.tv_myMsg)
        tv_myTime = itemView.findViewById(R.id.tv_myTime)
        cv_pic = itemView.findViewById(R.id.cv_pic)
    }






}