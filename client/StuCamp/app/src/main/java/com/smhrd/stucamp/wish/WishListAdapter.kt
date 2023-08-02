package com.smhrd.stucamp.wish

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.R
import com.smhrd.stucamp.VO.WishListVO
import com.smhrd.stucamp.VO.WishVO

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
    }
}