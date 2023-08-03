package com.smhrd.stucamp

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    lateinit var tvComId: TextView
    lateinit var tvComContent: TextView
    lateinit var btnCommentDel : Button


    init {
        tvComId = itemView.findViewById(R.id.tvComId)
        tvComContent = itemView.findViewById(R.id.tvComContent)
        btnCommentDel = itemView.findViewById(R.id.btnCommentDel)
    }
}
