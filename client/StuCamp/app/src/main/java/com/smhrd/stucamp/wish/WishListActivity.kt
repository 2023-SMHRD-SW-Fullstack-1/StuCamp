package com.smhrd.stucamp.wish

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.MainActivity
import com.smhrd.stucamp.MyFeedAdapter
import com.smhrd.stucamp.R
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.UserVO
import com.smhrd.stucamp.VO.WishListVO
import com.smhrd.stucamp.VO.WishVO
import org.json.JSONArray
import org.json.JSONObject

class WishListActivity : AppCompatActivity() {

    lateinit var reqQueue : RequestQueue
    lateinit var rc : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)
        rc = findViewById(R.id.rc)

        reqQueue = Volley.newRequestQueue(this)

        //wishList에 들어오면 해당 id의 wishlist 보여주기

        //spf(로그인한 이메일 가져오기)
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

        val wishList = ArrayList<WishListVO>()

        //mywish 불러오기(서버 통신)
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.42:8888/wish/$user_email",
            { response ->
                Log.d("response232323", response.toString())
                val result = JSONArray(response)

                for (i in 0 until result.length()) {
                    val wish = result.getJSONObject(i)
                    val feed_img = wish.getString("feed_imgpath").toString()
                    val feed_id = wish.getInt("feed_id")
                    wishList.add(WishListVO(feed_img, feed_id))
                }
                Log.d("wishList", wishList.toString())
                val adapter = WishListAdapter(wishList, this)
                rc.layoutManager = LinearLayoutManager(this)
                rc.adapter = adapter
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(request)
    }
}