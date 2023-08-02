package com.smhrd.stucamp

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    lateinit var rv : RecyclerView
    lateinit var tvId : TextView
    //lateinit var ivFeed : ImageView
    lateinit var ibHeart : ImageButton
    lateinit var ivFeed : ImageView
    lateinit var tvLikeCnt : TextView
    lateinit var tvContent : TextView
    //lateinit var edtComment : EditText

    init {
        tvId = itemView.findViewById(R.id.tvId2)
        //ivFeed = itemView.findViewById(R.id.ivFeed)
        ibHeart = itemView.findViewById(R.id.ibHeart2)
        ivFeed = itemView.findViewById(R.id.ivFeed2)
        tvLikeCnt = itemView.findViewById(R.id.tvLikeCnt2)
        tvContent = itemView.findViewById(R.id.tvContent2)
        //edtComment = itemView.findViewById(R.id.edtComment)
    }
}