package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedDelVO
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.MyFeedVO
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject

class MyFeedAdapter (var datas : ArrayList<FeedVO>, var context : Context)
    : RecyclerView.Adapter<MyFeedViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFeedViewHolder {
        return MyFeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.my_feed_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyFeedViewHolder, position: Int) {

        var feed : FeedVO = datas.get(position)

        var tvId2 : TextView = holder.tvId2
        //var ivFeed : ImageView = holder.ivFeed
        var ibHeart2 : ImageButton = holder.ibHeart2
        var tvLikeCnt2 : TextView = holder.tvLikeCnt2
        var tvContent2 : TextView = holder.tvContent2
        //var edtComment : EditText = holder.edtComment
        var btnFeedUpate : Button = holder.btnFeedUpdate
        var btnFeedDelete : Button = holder.btnFeedDelete


        var reqQueue : RequestQueue = Volley.newRequestQueue(context)

        //SharedPreference 생성
        val spf = context.getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

//        tvLikeCnt2.text = myFeed.feedLikeCnt.toString()
//        tvContent2.text = myFeed.feedContent

        //피드 수정
        btnFeedUpate.setOnClickListener{
            val intent = Intent(context, MyFeedUpdateActivity::class.java)
            context.startActivity(intent)
        }

        //피드 삭제
        btnFeedDelete.setOnClickListener {
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.42:8888/feed/delete",
                {
                        response ->
                    Log.d("response", response)
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()

                    if(response.equals("-1")) {
                        Toast.makeText(context, "아이디나 비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
                    }else{

                    }

                },
                {
                        error ->
                    Log.d("error", error.toString())
                    Toast.makeText(context, "에러발생!", Toast.LENGTH_LONG).show()
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params : MutableMap<String, String> = HashMap()
                    val user : FeedDelVO = FeedDelVO(feed.feed_id, user_email)
                    params.put("loginUser", Gson().toJson(user))

                    return params
                }
            }
            reqQueue.add(request)
        }
    }

}
