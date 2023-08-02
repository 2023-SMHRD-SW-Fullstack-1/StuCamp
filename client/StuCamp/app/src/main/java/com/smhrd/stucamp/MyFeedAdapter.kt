package com.smhrd.stucamp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.MyFeedVO

class MyFeedAdapter (var datas : ArrayList<MyFeedVO>, var context : Context)
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
        var tvId2 : TextView = holder.tvId2
        //var ivFeed : ImageView = holder.ivFeed
        var ibHeart2 : ImageButton = holder.ibHeart2
        var tvLikeCnt2 : TextView = holder.tvLikeCnt2
        var tvContent2 : TextView = holder.tvContent2
        //var edtComment : EditText = holder.edtComment

        var myFeed : MyFeedVO = datas.get(position)

        tvId2.text = myFeed.feedId

        tvLikeCnt2.text = myFeed.feedLikeCnt.toString()
        tvContent2.text = myFeed.feedContent
    }

}
