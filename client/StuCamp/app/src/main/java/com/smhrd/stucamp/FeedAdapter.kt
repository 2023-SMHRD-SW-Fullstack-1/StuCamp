package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.LikeVO
import com.smhrd.stucamp.VO.UserVO
import com.smhrd.stucamp.VO.WishVO
import org.w3c.dom.Comment

class FeedAdapter(var datas : ArrayList<FeedVO>, var context : Context, var activity: Fragment1)
    : RecyclerView.Adapter<FeedViewHolder>(){

    lateinit var reqQueue : RequestQueue

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        var tvId : TextView = holder.tvId
        var ibHeart : ImageButton = holder.ibHeart
        var ivFeed : ImageView = holder.ivFeed
        var tvLikeCnt : TextView = holder.tvLikeCnt
        var tvContent : TextView = holder.tvContent
        //var edtComment : EditText = holder.edtComment
        var btnCom : Button = holder.btnCom
        var btnAddWish : Button = holder.btnAddWish

        reqQueue = Volley.newRequestQueue(context)
        var feed : FeedVO = datas.get(position)

        tvId.text = feed.user_nickname
        val heartOn = ContextCompat.getDrawable(context, R.drawable.heart_on)
        val heartOff = ContextCompat.getDrawable(context, R.drawable.heart_off)
//        var isLiked : Boolean = false

        //feed id 가져오기
        val feedId = feed.feed_id

        //SharedPreference 생성
        val spf = context.getSharedPreferences("mySPF", Context.MODE_PRIVATE)

        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email
        //좋아요 정보 저장 spf
        var like = spf.getBoolean(user.user_email, false) //spf 초기세팅
        val editor = spf.edit()

        if(like){
            ibHeart.setImageDrawable(heartOn)
        }else{
            ibHeart.setImageDrawable(heartOff)
        }

        tvLikeCnt.text = feed.feed_like_cnt.toString()
        tvContent.text = feed.feed_content
//        Log.d("1111sfeed", feed.toString())

        tvId.text = feed.user_nickname.toString()
        //이미지 변환
        val imageBytes = Base64.decode(feed.feed_img.toString(), 0)
        val feed_img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ivFeed.setImageBitmap(feed_img)

//        ivFeed.setImageBitmap(feed_img)
        tvLikeCnt.text = feed.feed_like_cnt.toString()
        tvContent.text = feed.feed_content
        //edtComment.text = feed.edtComment

        //찜하기 버튼 클릭 => 찜목록에 추가
        btnAddWish.setOnClickListener {

            //서버와 통신
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.42:8888/wish/add",
                {
                        response ->
//                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()
                    if(response == "1"){
                        //좋아요 클릭 여부 spf 생성
                        Log.d("response", response)
                        btnAddWish.text = "찜완료"
                    }
                },{
                        error ->
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    val wish: WishVO = WishVO(user_email, feedId)
                    params.put("wish", Gson().toJson(wish))
                    Log.d("params", wish.toString())
                    return params
                }
            }
            reqQueue.add(request)
        }

        //댓글 버튼 클릭
        btnCom.setOnClickListener(){
            val intent = Intent(context, CommentActivity::class.java)
            context.startActivity(intent)
        }

        ibHeart.setOnClickListener(){

            if(!like){ //좋아요 클릭했을 때
                ibHeart.setImageDrawable(heartOn)
                feed.feed_like_cnt++
                notifyItemChanged(position)
                editor.putBoolean(user.user_email, true)
                editor.commit()

                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.42:8888/like/add",
                    {
                        response ->
//                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()
                        if(response == "1"){
                            //좋아요 클릭 여부 spf 생성
                        }
                    },{
                        error ->
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        val addLike: LikeVO = LikeVO(user_email, feedId)
                        params.put("addLike", Gson().toJson(addLike))
                        Log.d("params", addLike.toString())
                        return params
                    }
                }
                reqQueue.add(request)

            }else { //좋아요 취소했을 때
                ibHeart.setImageDrawable(heartOff)
                feed.feed_like_cnt--
                notifyItemChanged(position)
                editor.putBoolean(user.user_email, false)
                editor.commit()

                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.42:8888/like/cancel",
                    {
                            response ->
                            if(response == "1"){
                            }
                    },{
                            error ->
                        Log.d("error", error.toString())
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getParams(): MutableMap<String, String>? {
                        val params : MutableMap<String, String> = HashMap()
                        val cancelLike : LikeVO = LikeVO(user_email, feedId)
                        params.put("cancelLike", Gson().toJson(cancelLike))
                        Log.d("params", cancelLike.toString())
                        return params
                    }
                }
                reqQueue.add(request)
            }
        }
    }
}