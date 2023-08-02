package com.smhrd.stucamp

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyFeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    lateinit var tvId2 : TextView
    //lateinit var ivFeed : ImageView
    lateinit var ibHeart2 : ImageButton
    lateinit var tvLikeCnt2 : TextView
    lateinit var tvContent2 : TextView
    //lateinit var edtComment : EditText
    lateinit var btnFeedDelete : Button
    lateinit var btnFeedUpdate : Button

    init {
        tvId2 = itemView.findViewById(R.id.tvId2)
        //ivFeed = itemView.findViewById(R.id.ivFeed)
        ibHeart2 = itemView.findViewById(R.id.ibHeart2)
        tvLikeCnt2 = itemView.findViewById(R.id.tvLikeCnt2)
        tvContent2 = itemView.findViewById(R.id.tvContent2)
        btnFeedDelete = itemView.findViewById(R.id.btnFeedDelete)
        btnFeedUpdate = itemView.findViewById(R.id.btnFeedUpdate)
        //edtComment = itemView.findViewById(R.id.edtComment)
    }
}