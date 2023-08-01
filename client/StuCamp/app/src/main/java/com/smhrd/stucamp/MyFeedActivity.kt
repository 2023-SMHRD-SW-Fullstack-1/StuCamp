package com.smhrd.stucamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.VO.FeedVO
import com.smhrd.stucamp.VO.MyFeedVO

class MyFeedActivity : AppCompatActivity() {

    lateinit var rc : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feed)

        rc = findViewById(R.id.rcMyFeed)

        val feedList = ArrayList<MyFeedVO>()

        feedList.add(MyFeedVO("hihi", 3, "하이하이"))
        feedList.add(MyFeedVO("ID1", 2, "하이하이1"))
        feedList.add(MyFeedVO("ID2", 5, "하이하이2"))
        feedList.add(MyFeedVO("ID3", 3, "하이하이3"))

        val adapter = MyFeedAdapter(feedList, this)
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = adapter
    }
}