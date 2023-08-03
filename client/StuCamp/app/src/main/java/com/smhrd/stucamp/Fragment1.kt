package com.smhrd.stucamp
// 현록

import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedDelVO
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.UserVO
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

        //spf(로그인한 이메일 가져오기)
        val spf = requireActivity().getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
//        val user_email = user.user_email
//        Log.d("user_email",user_email)

        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.22:8888/feed/findall",
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
                val adapter = FeedAdapter(feedList, requireActivity(), Fragment1())
                rc.layoutManager = LinearLayoutManager(requireActivity())
                rc.adapter = adapter
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {
        }

        reqQueue.add(request)

        return view
    }


}