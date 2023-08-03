package com.smhrd.stucamp

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvComId: TextView
    val tvComContent: TextView


    init {
        tvComId = itemView.findViewById(R.id.tvComId)
        tvComContent = itemView.findViewById(R.id.tvComContent)
//        rvComment = itemView.findViewById(R.id.rvComment)
//        etComment = itemView.findViewById(R.id.etComment)
//        btnCommentSend = itemView.findViewById(R.id.btnCommentSend)
    }
}
