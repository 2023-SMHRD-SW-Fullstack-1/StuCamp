package com.smhrd.stucamp
// 현록

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.stucamp.VO.FeedVO

class Fragment1 : Fragment() {

    lateinit var rc : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_1, container, false)

        rc = view.findViewById(R.id.rcFeed)

        val feedList = ArrayList<FeedVO>()

        feedList.add(FeedVO("hihi", 3, "하이하이"))
        feedList.add(FeedVO("ID1", 2, "하이하이1"))
        feedList.add(FeedVO("ID2", 5, "하이하이2"))
        feedList.add(FeedVO("ID3", 3, "하이하이3"))

        val adapter = FeedAdapter(feedList, requireActivity())
        rc.layoutManager = LinearLayoutManager(requireActivity())
        rc.adapter = adapter


     return view
    }
}