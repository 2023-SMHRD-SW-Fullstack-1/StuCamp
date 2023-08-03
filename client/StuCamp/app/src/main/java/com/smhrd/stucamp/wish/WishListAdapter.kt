package com.smhrd.stucamp.wish

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.stucamp.FeedAdapter
import com.smhrd.stucamp.Fragment1
import com.smhrd.stucamp.R
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.WishListVO
import com.smhrd.stucamp.VO.WishVO
import org.json.JSONObject

class WishListAdapter (var datas : ArrayList<WishListVO>, var context: Context)
    : RecyclerView.Adapter<WishListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wish_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        var feedImg : ImageView = holder.feedImg
        var wishListVO : WishListVO = datas.get(position)

        //이미지처리
        val imageBytes = Base64.decode(wishListVO.feed_imgpath, 0)
        val feed_img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        Log.d("feed_img",feed_img.toString())
        feedImg.setImageBitmap(feed_img)

        //이미지 클릭하면 해당 피드 보여주기
        feedImg.setOnClickListener{
            val feed_id = wishListVO.feed_id.toString()

            //상세 피드로 이동
            val intentWish = Intent(context, WishDetailActivity::class.java)
            intentWish.putExtra("wish_feed_id", feed_id)
            context.startActivity(intentWish)
        }
    }
}