package com.smhrd.stucamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bnv : BottomNavigationView
    lateinit var fl : FrameLayout

    lateinit var tvId : TextView
    lateinit var btnMyPage : Button
    lateinit var btnWrite : Button
    lateinit var btnMyHome : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnv = findViewById(R.id.bnv)
        fl = findViewById(R.id.wishFl)

        //tvId = findViewById(R.id.tvId)
        btnMyPage = findViewById(R.id.btnMyPage)
        btnWrite = findViewById(R.id.btnWrite)
        btnMyHome = findViewById(R.id.btnMyHome)


        supportFragmentManager.beginTransaction().replace(
            R.id.wishFl,
            Fragment1()
        ).commit()


        //bottomNavigationView 이동
        bnv.setOnItemSelectedListener {
            Log.d("id", it.itemId.toString())

           when(it.itemId){
                R.id.tab1 -> {
                    Log.d("id", "1")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.wishFl,
                        Fragment1()
                    ).commit()
                }
                R.id.tab2 -> {
                    Log.d("id", "2")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.wishFl,
                        Fragment2()
                    ).commit()
                }
                R.id.tab3 -> {
                    Log.d("id", "3")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.wishFl,
                        Fragment3()
                    ).commit()
                }
                R.id.tab4 -> {
                    Log.d("id", "4")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.wishFl,
                        Fragment4()
                    ).commit()
                }
            }

            true
        }

        // 마이피드
        btnMyHome.setOnClickListener(){
            var intent = Intent(this, MyFeedActivity::class.java)
            startActivity(intent)
        }

        // 글쓰기 버튼 클릭
        btnWrite.setOnClickListener(){
            var intent = Intent(this, FeedWriteActivity::class.java)
            startActivity(intent)
        }


        // MyPage 버튼 클릭
        btnMyPage.setOnClickListener(){

            var intent = Intent(this, MemInfoActivity::class.java)
            startActivity(intent)
        }

    }
}