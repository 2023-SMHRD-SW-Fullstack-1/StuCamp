package com.smhrd.stucamp

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    lateinit var tvId : TextView
    lateinit var ivFeed : ImageView
    lateinit var tvLikeCnt : TextView
    lateinit var tvContent : TextView
    //lateinit var edtComment : EditText

    init {
        tvId = itemView.findViewById(R.id.tvId)
        ivFeed = itemView.findViewById(R.id.ivFeed)
        tvLikeCnt = itemView.findViewById(R.id.tvLikeCnt)
        tvContent = itemView.findViewById(R.id.tvContent)
        //edtComment = itemView.findViewById(R.id.edtComment)
    }
}