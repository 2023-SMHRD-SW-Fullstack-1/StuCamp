package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.CommentResVO
import com.smhrd.stucamp.VO.CommentVO
import com.smhrd.stucamp.VO.UserVO

class CommentResAdapter (var datas: ArrayList<CommentResVO>, var context : Context)
    : RecyclerView.Adapter<CommentViewHolder>(){

    lateinit var tvComId : TextView
    lateinit var tvComContent : TextView
    lateinit var btnCommentDel : Button
    lateinit var reqQueue : RequestQueue

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
        btnCommentDel = holder.btnCommentDel
        reqQueue = Volley.newRequestQueue(context)

        var commentRes: CommentResVO = datas.get(position)
        val comment_writer = datas.get(position).user_email

        tvComId.text = commentRes.user_nickname
        tvComContent.text = commentRes.comment_content

        val comment_id = datas.get(position).comment_id.toString()

        //본인이 작성한 댓글만 삭제 버튼 보이기
        //SharedPreference 생성
        val spf = context.getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email.toString()
        Log.d("user_email",user_email)

        if(comment_writer.toString().equals(user_email)){
            btnCommentDel.visibility = View.VISIBLE
        }else{
            btnCommentDel.visibility = View.INVISIBLE
        }

        //댓글 삭제하기
        btnCommentDel.setOnClickListener {
            val request = object : StringRequest(
                Request.Method.GET,
                "http://172.30.1.25:8888/comment/delete/$comment_id",
                { response ->
                    if (response == "1") {
                        datas.removeAt(position)
                        notifyDataSetChanged()
                    }
                },
                { error ->
                    Log.d("error", error.toString())
                }
            ){}
            reqQueue.add(request)
        }
    }
}