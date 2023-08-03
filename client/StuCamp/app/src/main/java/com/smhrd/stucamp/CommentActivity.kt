package com.smhrd.stucamp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.smhrd.stucamp.VO.CommentVO
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject

class CommentActivity : AppCompatActivity() {

    lateinit var etComment: EditText
    lateinit var btnCommentSend: Button
    lateinit var rvComment: RecyclerView
    lateinit var adapter: CommentAdapter
    val commentList: ArrayList<CommentVO> = ArrayList()

    lateinit var reqQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        etComment = findViewById(R.id.etComment)
        btnCommentSend = findViewById(R.id.btnCommentSend)
        rvComment = findViewById(R.id.rvComment)

        // spf 처리
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = spf.getString("user", " ")
        val userVO = Gson().fromJson(user, UserVO::class.java)

        val userEmail = userVO.user_email

        reqQueue = Volley.newRequestQueue(this)

        // 서버에서 댓글 데이터를 가져옵니다. 필요에 따라 URL, 서버 설정 등을 확인하고 수정하십시오.
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.50:8888/comment/add",
            { response ->
                Log.d("response", response.toString())
                val result = JSONObject(response).getJSONArray("commentAll")
                for (i in 0 until result.length()) {
                    val comment = result.getJSONObject(i)
                    val user_id = comment.getString("user_id").toString()
                    val comment_content = comment.getString("comment_content").toString()
                    commentList.add(CommentVO(user_id, comment_content))
                }
                Log.d("commentList", commentList.toString())
                adapter.notifyDataSetChanged()
            },
            { error -> Log.d("error", error.toString()) }
        ) {}

        reqQueue.add(request)

        adapter = CommentAdapter(commentList, this)
        rvComment.layoutManager = LinearLayoutManager(this)
        rvComment.adapter = adapter

        btnCommentSend.setOnClickListener {
            val commentText = etComment.text.toString()
            if (commentText.isNotEmpty()) {
                // 서버에 데이터 전송 및 목록 업데이트 작업을 수행합니다.
                val user_id = userEmail // 이 부분을 수정하십시오.
                val comment_content = etComment.text.toString()
                commentList.add(CommentVO(user_id, comment_content))
                adapter.notifyDataSetChanged()
                etComment.text.clear()
            } else {
                Toast.makeText(this, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}