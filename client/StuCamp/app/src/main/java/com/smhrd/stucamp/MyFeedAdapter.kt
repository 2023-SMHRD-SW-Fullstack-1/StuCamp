package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedDelVO
import com.smhrd.stucamp.VO.FeedUpdateVO
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
        var ivFeed : ImageView = holder.ivFeed
        var ibHeart2 : ImageButton = holder.ibHeart2
        var tvLikeCnt2 : TextView = holder.tvLikeCnt2
        var tvContent2 : TextView = holder.tvContent2
        //var edtComment : EditText = holder.edtComment
        var btnFeedUpate : Button = holder.btnFeedUpdate
        var btnFeedDelete : Button = holder.btnFeedDelete
        var btnFeedUpdate : Button = holder.btnFeedUpdate


        var reqQueue : RequestQueue = Volley.newRequestQueue(context)

        //SharedPreference 생성
        val spf = context.getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

//        tvLikeCnt2.text = myFeed.feedLikeCnt.toString()
//        tvContent2.text = myFeed.feedContent

        //데이터 저장
        tvId2.text = feed.user_nickname
        tvLikeCnt2.text = feed.feed_like_cnt.toString()
        tvContent2.text = feed.feed_content
        //이미지 변환
        val imageBytes = Base64.decode(feed.feed_img, 0)
        val feed_img2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ivFeed.setImageBitmap(feed_img2)

        //피드 삭제
        btnFeedDelete.setOnClickListener {
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.52:8888/feed/delete",
                {
                        response ->
                    Log.d("response", response)
//                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                    if(response == "1"){
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                    if(response.equals("0")) {
                        Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
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
                    val deleteFeed : FeedDelVO = FeedDelVO(feed.feed_id, user_email)
                    params.put("deleteFeed", Gson().toJson(deleteFeed))

                    return params
                }
            }
            reqQueue.add(request)
        }

        //피드 수정 -> 페이지 이동
        btnFeedUpdate.setOnClickListener {
            val intent = Intent(context, MyFeedUpdateActivity::class.java)
            intent.putExtra("feed_id", feed.feed_id)
            intent.putExtra("feed_content", feed.feed_content)
            intent.putExtra("feed_img", feed.feed_img)
            context.startActivity(intent)
        }

    }
}
