package com.smhrd.stucamp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.VO.CommentResVO

class CommentResAdapter (var datas: ArrayList<CommentResVO>, var context : Context)
    : RecyclerView.Adapter<CommentViewHolder>(){

    lateinit var tvComId : TextView
    lateinit var tvComContent : TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        tvComId = holder.tvComId
        tvComContent = holder.tvComContent

        var commentRes : CommentResVO = datas.get(position)

        tvComId.text = commentRes.user_nickname
        tvComContent.text = commentRes.comment_content
    }

}