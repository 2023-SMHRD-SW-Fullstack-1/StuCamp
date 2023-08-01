package com.smhrd.stucamp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.smhrd.stucamp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //인트로 화면을 몇초동안 보여주고 넘어가고 싶다 => Handler() 사용
        val handler = Handler()
        handler.postDelayed({ //말 그대로 지연시키기({딜레이 후 뭘 할건지}, 몇 초동안 딜레이 시킬건지
            // Intro 화면이 보이고 3초 뒤에 MainActivity 전환 -> Intent
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } , 3000)
    }
}