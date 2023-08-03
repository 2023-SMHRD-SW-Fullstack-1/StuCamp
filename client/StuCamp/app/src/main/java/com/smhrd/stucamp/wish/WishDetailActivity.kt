package com.smhrd.stucamp.wish

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.stucamp.MainActivity
import com.smhrd.stucamp.R
import org.json.JSONObject

class WishDetailActivity : AppCompatActivity() {

    lateinit var tvId : TextView
    lateinit var ivFeed : ImageView
    lateinit var tvLikeCnt : TextView
    lateinit var tvContent : TextView
    lateinit var reqQueue : RequestQueue
    lateinit var btnMoveWish : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_detail)

        reqQueue = Volley.newRequestQueue(this)
        tvId = findViewById(R.id.tv_roomTitle)
        ivFeed = findViewById(R.id.ivFeed2)
        tvLikeCnt = findViewById(R.id.tvLikeCnt2)
        tvContent = findViewById(R.id.tvContent2)
        btnMoveWish = findViewById(R.id.btnMoveWish)

        val it = intent
        val feed_id = it.getStringExtra("wish_feed_id").toString()
        Log.d("feedid:", feed_id)

        //서버 통신
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.25:8888/feed/find/$feed_id",
            { response ->
                Log.d("response", response.toString())
                val feed = JSONObject(response)
                val user = feed.getJSONObject("user")

                Log.d("user", user.toString())

                val feed_content = feed.getString("feed_content").toString()
                val feed_imgpath = feed.getString("feed_imgpath").toString()
                val user_nickname = feed.getJSONObject("user").getString("user_nickname")
                val feed_like_cnt = feed.getInt("feed_like_cnt")

                //이미지 변환
                val imageBytes = Base64.decode(feed_imgpath, 0)
                val feed_img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ivFeed.setImageBitmap(feed_img)

                //view와 연결
                tvId.text = user_nickname
                tvLikeCnt.text = feed_like_cnt.toString()
                tvContent.text = feed_content

            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {
        }

        reqQueue.add(request)

        btnMoveWish.setOnClickListener {
            val intent = Intent(this, WishListActivity::class.java)
            startActivity(intent)
        }
    }
}