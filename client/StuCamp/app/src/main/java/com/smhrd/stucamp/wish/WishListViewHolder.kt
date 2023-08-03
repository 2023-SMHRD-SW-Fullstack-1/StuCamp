package com.smhrd.stucamp.wish

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.R

class WishListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var feedImg: ImageView

    init{
        feedImg = itemView.findViewById(R.id.feedImg)
    }


}