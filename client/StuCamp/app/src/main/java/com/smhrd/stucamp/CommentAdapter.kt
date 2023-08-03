package com.smhrd.stucamp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.stucamp.VO.CommentVO
import com.smhrd.stucamp.VO.FeedVO

class CommentAdapter(val datas: ArrayList<CommentVO>, val context: Context) :
    RecyclerView.Adapter<CommentViewHolder>() {

    lateinit var reqQueue: RequestQueue

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment: CommentVO = datas[position]

        reqQueue = Volley.newRequestQueue(context)

        holder.tvComId.text = comment.user_id
        holder.tvComContent.text = comment.comment_content
    }
}