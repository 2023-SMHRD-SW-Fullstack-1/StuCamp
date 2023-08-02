package com.smhrd.stucamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.MyFeedVO
import org.json.JSONObject

class MyFeedActivity : AppCompatActivity() {

    lateinit var rc : RecyclerView
    lateinit var btnFeedUpdate : Button
    lateinit var btnFeedDelete : Button

    lateinit var reqQueue: RequestQueue
    lateinit var myFeedList: ArrayList<MyFeedVO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feed)

        rc = findViewById(R.id.rcMyFeed)

        reqQueue = Volley.newRequestQueue(this@MyFeedActivity)
        myFeedList = ArrayList<MyFeedVO>()

        val myFeedList = ArrayList<MyFeedVO>()

//        val request = object : StringRequest(
//            Request.Method.GET,
//            "http://172.30.1.42:8888/myfeed/findall",
//            { response ->
//                Log.d("response", response.toString())
//                val result = JSONObject(response).getJSONArray("feedDetails")
//
//                for (i in 0 until result.length()) {
//                    val feed = result.getJSONObject(i)
//                    val feed_content = feed.getString("feed_content").toString()
//                    val feed_img = feed.getString("feed_imgpath").toString()
//                    val user_nickname = feed.getJSONObject("user").getString("user_nickname")
//                    val feed_like_cnt = feed.getInt("feed_like_cnt")
//                    val comment = feed.getJSONArray("comment")
//
//                    myFeedList.add(MyFeedVO(user_nickname, feed_like_cnt, feed_content, feed_img))
//                    Log.d("feed", feed.toString())
//
//                }
//                Log.d("myFeedList", feedList.toString())
//                val adapter = FeedAdapter(myFeedList, this@MyFeedActivity)
//                rc.layoutManager = LinearLayoutManager(this@MyFeedActivity)
//                rc.adapter = adapter
//            },
//            { error ->
//                Log.d("error", error.toString())
//            }
//        ) {}
//
//        reqQueue.add(request)

        myFeedList.add(MyFeedVO("hihi", 3, "하이하이"))
        myFeedList.add(MyFeedVO("ID1", 2, "하이하이1"))
        myFeedList.add(MyFeedVO("ID2", 5, "하이하이2"))
        myFeedList.add(MyFeedVO("ID3", 3, "하이하이3"))

        val adapter = MyFeedAdapter(myFeedList, this)
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = adapter




    }
}
