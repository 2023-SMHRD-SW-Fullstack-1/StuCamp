package com.smhrd.stucamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smhrd.stucamp.VO.CommentVO
import org.json.JSONObject

class CommentActivity : AppCompatActivity() {

    lateinit var rc : RecyclerView
    lateinit var reqQueue: RequestQueue
    lateinit var etComment : EditText
    lateinit var btnCommentSend : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val bottomSheetView = layoutInflater.inflate(R.layout.activity_comment, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        rc = findViewById(R.id.rvComment)
        etComment = findViewById(R.id.etComment)
        btnCommentSend = findViewById(R.id.btnCommentSend)
        reqQueue = Volley.newRequestQueue(this)

        val commentList = ArrayList<CommentVO>()

//        commentList.add(CommentVO("hihi",  "하이하이"))
//        commentList.add(CommentVO("ID1",  "하이하이1"))
//        commentList.add(CommentVO("ID2",  "하이하이2"))
//        commentList.add(CommentVO("ID3",  "하이하이3"))


        //전체 댓글 불러오기
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.22:8888/comment/:feed_id",
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
                val adapter = CommentAdapter(commentList, this)
                rc.layoutManager = LinearLayoutManager(this)
                rc.adapter = adapter
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}

        reqQueue.add(request)

        //댓글 추가하기
        btnCommentSend.setOnClickListener() {
            val request1 = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.22:8888/comment/add",
                { response ->
                    Log.d("response", response.toString())
                    val result = JSONObject(response).getJSONArray("commentAdd")

                    for (i in 0 until result.length()) {
                        val comment = result.getJSONObject(i)
                        val user_id = comment.getString("user_id").toString()
                        val comment_content = etComment.text.toString()
                        commentList.add(CommentVO(user_id, comment_content))
                    }
                    Log.d("commentList", commentList.toString())
                    val adapter = CommentAdapter(commentList, this)
                    rc.layoutManager = LinearLayoutManager(this)
                    rc.adapter = adapter
                },
                { error ->
                    Log.d("error", error.toString())
                }
            ) {}

            reqQueue.add(request1)
        }
    }
}