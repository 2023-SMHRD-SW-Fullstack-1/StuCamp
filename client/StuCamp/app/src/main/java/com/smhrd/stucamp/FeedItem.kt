package com.smhrd.stucamp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedItem  : AppCompatActivity() {

    lateinit var ibHeart : ImageButton
    lateinit var tvLikeCnt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_item)

       // tvHeart = findViewById(R.id.tvHeart)
        tvLikeCnt = findViewById(R.id.tvLikeCnt)

        ibHeart.setOnClickListener(){
            onTextViewClicked()
        }

    }
    fun onTextViewClicked(){
        Toast.makeText(this,"TextView 클릭!", Toast.LENGTH_SHORT).show()

    }
}