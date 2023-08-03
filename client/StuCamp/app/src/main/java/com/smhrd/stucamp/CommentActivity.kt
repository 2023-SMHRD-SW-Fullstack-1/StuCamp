package com.smhrd.stucamp

import android.content.Context
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
import com.google.gson.Gson
import com.smhrd.stucamp.VO.CommentResVO
import com.smhrd.stucamp.VO.CommentVO
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class CommentActivity : AppCompatActivity() {

    lateinit var rvComment : RecyclerView
    lateinit var reqQueue: RequestQueue
    lateinit var etComment : EditText
    lateinit var btnCommentSend : Button
    lateinit var adapter: CommentAdapter
    lateinit var resAdapter : CommentResAdapter

    val commentList : ArrayList<CommentVO> = ArrayList()
    val commentResList : ArrayList<CommentResVO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        rvComment = findViewById(R.id.rvComment)
        etComment = findViewById(R.id.etComment)
        btnCommentSend = findViewById(R.id.btnCommentSend)
        reqQueue = Volley.newRequestQueue(this)

        //SharedPreference 생성
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email.toString()
        Log.d("user_email",user_email)

        val it = getIntent()
        val feed_id = it.getIntExtra("feed_id", 0)

        Log.d("ifeeeedddddddddddd", feed_id.toString())

        //전체 댓글 불러오기
        val request = object : StringRequest(
            Request.Method.GET,

            "http://172.30.1.25:8888/comment/$feed_id",
            { response ->
                val result = JSONObject(response).getJSONArray("commentDetails")

                Log.d("result", result.toString())

                for (i in 0 until result.length()) {
                    val commentJson = result.getJSONObject(i)
                    Log.d("comment 1개", commentJson.toString())
                    val comment = Gson().fromJson(commentJson.toString(), CommentResVO::class.java)
                    Log.d("comment(string)", comment.toString())
                    commentResList.add(comment)

                    Log.d("commentResList", commentResList.toString())
                }
                //                adapter.notifyDataSetChanged() // 어댑터에게 데이터가 변경되었음을 알림
                resAdapter = CommentResAdapter(commentResList, this)
                rvComment.layoutManager = LinearLayoutManager(this)
                rvComment.adapter = resAdapter
            },
            { error ->
                Log.d("feed_id22", feed_id.toString())
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(request)


        //댓글 추가하기
        btnCommentSend.setOnClickListener() {
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.25:8888/comment/add",
                { response ->
                    Log.d("responseAdd", response.toString())

                },
                { error ->
                    Log.d("error", error.toString())
                }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    //user_email : SPF 사용 해서 가져오고, content =>
                    val comment: CommentVO =
                        CommentVO(user_email, feed_id, etComment.text.toString())
                    params.put("addComment", Gson().toJson(comment))
                    etComment.text.clear()

                    return params
                }
            }

            reqQueue.add(request)
        }
    }

}