package com.smhrd.stucamp

import android.content.Context
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
        //var ivFeed : ImageView = holder.ivFeed
        var tvLikeCnt : TextView = holder.tvLikeCnt
        var tvContent : TextView = holder.tvContent
        //var edtComment : EditText = holder.edtComment

        var feed : FeedVO = datas.get(position)

        tvId.text = feed.feedId
        //ivFeed = feed.feedImgPath
        tvLikeCnt.text = feed.feedLikeCnt.toString()
        tvContent.text = feed.feedContent
        //edtComment.text = feed.edtComment


    }

}