package com.smhrd.stucamp
// 현록

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.stucamp.VO.FeedVO
import org.json.JSONObject

class Fragment1 : Fragment() {

    lateinit var rc: RecyclerView
    lateinit var reqQueue: RequestQueue
    lateinit var feedList: ArrayList<FeedVO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_1, container, false)

        rc = view.findViewById(R.id.rcFeed)
        reqQueue = Volley.newRequestQueue(requireActivity())
        feedList = ArrayList<FeedVO>()

        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.42:8888/feed/findall",
            { response ->
                Log.d("response", response.toString())
                val result = JSONObject(response).getJSONArray("feedDetails")

                for (i in 0 until result.length()) {
                    val feed = result.getJSONObject(i)
                    val feed_content = feed.getString("feed_content").toString()
                    val feed_img = feed.getString("feed_imgpath").toString()
                    val user_nickname = feed.getJSONObject("user").getString("user_nickname")
                    val feed_like_cnt = feed.getInt("feed_like_cnt")
                    val feed_id = feed.getInt("feed_id")
                    val comment = feed.getJSONArray("comment")
                    feedList.add(FeedVO(user_nickname, feed_like_cnt, feed_content, feed_img, feed_id))
                }
                Log.d("feedList", feedList.toString())
                val adapter = FeedAdapter(feedList, requireActivity())
                rc.layoutManager = LinearLayoutManager(requireActivity())
                rc.adapter = adapter
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}

        reqQueue.add(request)

        return view
    }
}