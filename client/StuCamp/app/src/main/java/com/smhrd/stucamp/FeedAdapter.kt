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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.LikeVO
import com.smhrd.stucamp.VO.UserVO
import com.smhrd.stucamp.VO.WishListVO
import com.smhrd.stucamp.VO.WishVO
import com.smhrd.stucamp.wish.WishListAdapter
import org.json.JSONArray
import org.w3c.dom.Comment

class FeedAdapter(var datas : ArrayList<FeedVO>, var context : Context, var activity: Fragment1)
    : RecyclerView.Adapter<FeedViewHolder>(){

    lateinit var reqQueue : RequestQueue
    val isLikedMap: MutableMap<Int, Boolean> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        //좋아요 여부 확인
//        var isLikedMap = isLikedMap[position] ?: false

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
        var isLiked : Boolean = false

        //feed id 가져오기
        val feed_id = feed.feed_id
        Log.d("feeddededed", feed_id.toString())

        //SharedPreference 생성
        val spf = context.getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

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

        //좋아요 여부 확인
        //1. 서버 통신
        var likeList : ArrayList<String> = ArrayList<String>()

        val likeRequest = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.42:8888/like/$feed_id",
            { response ->
                if(response != "-1"){
                    Log.d("wish 불러오기 response", response.toString())
                    val result = JSONArray(response)

                    for (i in 0 until result.length()) {
                        val like = result.getJSONObject(i)
                        val like_feed_id = like.getString("feed_id").toString()
                        likeList.add(like_feed_id)
                    }

                    //전체 피드 중 좋아요 여부 확인
                    for(like_feed_id : String in likeList){
                        Log.d("like = feedid", like_feed_id)
                        Log.d("feed_id", feed_id.toString())
                        if(like_feed_id == feed_id.toString()) {
                                ibHeart.setImageDrawable(heartOn)
                                isLiked = true
                        }else{
                            ibHeart.setImageDrawable(heartOff)
                            isLiked = false
                        }
                    }
            }
        },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(likeRequest)


        var wishList = ArrayList<WishListVO>()
        
        //찜버튼 텍스트
        var textBtnAddWish : String = "찜하기"

        //찜 여부 확인
        //1. 서버 통신
        val wishRequest = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.42:8888/wish/$user_email",
            { response ->
                Log.d(">>>>>>>>>>>>>>>", response.toString())
                if(response != "-1"){
                    Log.d("wish 불러오기 response", response.toString())
                    val result = JSONArray(response)

                    for (i in 0 until result.length()) {
                        val wish = result.getJSONObject(i)
                        val wish_feed_img = wish.getJSONObject("Feed").getString("feed_imgpath").toString()
                        val wish_feed_id = wish.getInt("feed_id")
                        wishList.add(WishListVO(wish_feed_img, wish_feed_id))
                    }
//                    Log.d("11111111111111111", wishList.get(1).feed_id.toString())
//                    Log.d("000000000000000000", wishList.get(0).feed_id.toString())
                    //2. 전체 피드 중 찜 여부 확인

                    for(wish : WishListVO in wishList){
//                    Log.d("반복문 실행 여부 확인 ", feed_id.toString())
//                        Log.d("반복문 실행 여부 확인 1", wish.feed_id.toString())
//                        Log.d("반복문 실행 여부 확인 2", feed_id.toString())
                        if(wish.feed_id.toString().equals(feed_id.toString())) {
                            Log.d("같다 : ", wish.feed_id.toString())
                            textBtnAddWish = "찜취소"
                        }
//                        }else{
//                            Log.d("다르다", wish.feed_id.toString())
//                            textBtnAddWish = "찜하기"
//                        }
                        btnAddWish.text = textBtnAddWish
                    }
//                btnAddWish.text = textBtnAddWish

                }

            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(wishRequest)

        //찜하기 버튼 클릭 이벤트
        btnAddWish.setOnClickListener {
            //찜하기 버튼일 경우
            if(btnAddWish.text.equals("찜하기")){
                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.42:8888/wish/add",
                    {
                            response ->
                        if(response == "1"){
                            //좋아요 클릭 여부 spf 생성
                            Log.d("response", response)
                            btnAddWish.text = "찜완료"
                            Toast.makeText(context, "찜목록에 추가되었습니다", Toast.LENGTH_SHORT).show()
                        }else if(response == "-1"){
                            Toast.makeText(context, "이미 찜 목록에 있습니다", Toast.LENGTH_SHORT).show()
                        }
                    },{
                            error ->
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        val wish: WishVO = WishVO(user_email, feed_id)
                        params.put("wish", Gson().toJson(wish))
                        Log.d("params", wish.toString())
                        return params
                    }
                }
                reqQueue.add(request)
            }else{ //찜 취소 버튼일 경우
                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.42:8888/wish/delete",
                    {
                            response ->
                        if(response == "1"){
                            //좋아요 클릭 여부 spf 생성
                            Log.d("response", response.toString())
                            btnAddWish.text = "찜하기"
                            Toast.makeText(context, "찜목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show()
                        }
                    },{
                            error ->
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        val wish: WishVO = WishVO(user_email, feed_id)
                        params.put("wish", Gson().toJson(wish))
                        Log.d("params", wish.toString())
                        return params
                    }
                }
                reqQueue.add(request)
            }
        }

        //댓글 버튼 클릭
        btnCom.setOnClickListener(){
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("feed_id", feed_id)
            context.startActivity(intent)
        }

        //좋아요 클릭
        ibHeart.setOnClickListener(){
            Log.d("ibHeart", ibHeart.context.toString())

            if(!isLiked){ //좋아요 클릭했을 때
                ibHeart.setImageDrawable(heartOn)
                feed.feed_like_cnt++
                notifyItemChanged(position)
                isLiked = true

                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.25:8888/like/add",
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
                        val addLike: LikeVO = LikeVO(user_email, feed_id)
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
                isLiked = false

                //서버와 통신
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.25:8888/like/cancel",
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
                        val cancelLike : LikeVO = LikeVO(user_email, feed_id)
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