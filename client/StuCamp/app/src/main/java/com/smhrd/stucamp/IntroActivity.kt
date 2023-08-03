package com.smhrd.stucamp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // 애니메이션 로드
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        findViewById<ImageView>(R.id.tvDelete).startAnimation(fadeInAnimation)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 1500)
    }
}