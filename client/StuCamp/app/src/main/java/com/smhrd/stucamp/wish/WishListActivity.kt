package com.smhrd.stucamp.wish

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
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
    lateinit var btnMoveMain : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)
        rc = findViewById(R.id.rc)
        btnMoveMain = findViewById(R.id.btnMoveMain)

        reqQueue = Volley.newRequestQueue(this)

        //wishList에 들어오면 해당 id의 wishlist 보여주기

        //spf(로그인한 이메일 가져오기)
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

        val wishList = ArrayList<WishListVO>()

        //main버튼 클릭시 main 뷰로 이동
        btnMoveMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //mywish 불러오기(서버 통신)
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.22:8888/wish/$user_email",
            { response ->
                Log.d("wish 불러오기 response", response.toString())
                if(response == "-1"){
                    Toast.makeText(this, "저장된 피드가 없습니다",Toast.LENGTH_SHORT).show()
                    finish()

                }else{
                    val result = JSONArray(response)

                    for (i in 0 until result.length()) {
                        val wish = result.getJSONObject(i)
                        val feed_img = wish.getJSONObject("Feed").getString("feed_imgpath").toString()
                        val feed_id = wish.getInt("feed_id")
                        wishList.add(WishListVO(feed_img, feed_id))
                    }
                    Log.d("wishList", wishList.toString())
                    val adapter = WishListAdapter(wishList, this)
                    rc.layoutManager = GridLayoutManager(this, 3)
                    rc.adapter = adapter
                }
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(request)
    }
}