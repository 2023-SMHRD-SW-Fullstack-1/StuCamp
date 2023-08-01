package com.smhrd.stucamp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.VO.FeedVO

class FeedAdapter(var datas : ArrayList<FeedVO>, var context : Context)
    : RecyclerView.Adapter<FeedViewHolder>(){
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
        var ivFeed : ImageView = holder.ivFeed
        var tvLikeCnt : TextView = holder.tvLikeCnt
        var tvContent : TextView = holder.tvContent
        //var edtComment : EditText = holder.edtComment

        var feed : FeedVO = datas.get(position)

        Log.d("1111sfeed", feed.toString())

        tvId.text = feed.user_nickname.toString()
        //이미지 변환
//        val imageBytes = Base64.decode(feed.feed_imgpath.toString(), 0)
//        val feed_img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

//        ivFeed.setImageBitmap(feed_img)
        tvLikeCnt.text = feed.feed_like_cnt.toString()
        tvContent.text = feed.feed_content
        //edtComment.text = feed.edtComment


    }

}