package com.smhrd.stucamp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.CommentResVO
import com.smhrd.stucamp.VO.CommentVO
import com.smhrd.stucamp.VO.FeedVO

class CommentAdapter(var datas: ArrayList<CommentVO>, var context: Context)
    : RecyclerView.Adapter<CommentViewHolder>(){

    lateinit var reqQueue : RequestQueue
    lateinit var rvComment : RecyclerView
    lateinit var etComment : EditText
    lateinit var btnCommentSend : Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var tvComId : TextView = holder.tvComId
        var tvComContent : TextView = holder.tvComContent

    }
}