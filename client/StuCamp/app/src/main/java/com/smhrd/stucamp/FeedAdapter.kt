package com.smhrd.stucamp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        //var ivFeed : ImageView = holder.ivFeed
        var ibHeart : ImageButton = holder.ibHeart
        var tvLikeCnt : TextView = holder.tvLikeCnt
        var tvContent : TextView = holder.tvContent
        //var edtComment : EditText = holder.edtComment

        var feed : FeedVO = datas.get(position)

        tvId.text = feed.feedId
        //ivFeed = feed.feedImgPath
        val heartOn = ContextCompat.getDrawable(context, R.drawable.heart_on)
        val heartOff = ContextCompat.getDrawable(context, R.drawable.heart_off)
        var isLiked : Boolean = false

        ibHeart.setOnClickListener(){
            if(!isLiked){
                ibHeart.setImageDrawable(heartOn)
                isLiked = true
            }else {
                ibHeart.setImageDrawable(heartOff)
                isLiked = false
            }

        }

        tvLikeCnt.text = feed.feedLikeCnt.toString()
        tvContent.text = feed.feedContent
        //edtComment.text = feed.edtComment


    }

}