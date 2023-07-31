package com.smhrd.stucamp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bnv : BottomNavigationView
    lateinit var fl : FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnv = findViewById(R.id.bnv)
        fl = findViewById(R.id.fl)


        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            Fragment1()
        ).commit()
        //bottomNavigationView 이동
        bnv.setOnItemSelectedListener {
            Log.d("id", it.itemId.toString())

           when(it.itemId){
                R.id.tab1 -> {
                    Log.d("id", "1")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment1()
                    ).commit()
                }
                R.id.tab2 -> {
                    Log.d("id", "2")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment2()
                    ).commit()
                }
                R.id.tab3 -> {
                    Log.d("id", "3")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment3()
                    ).commit()
                }
                R.id.tab4 -> {
                    Log.d("id", "4")
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment4()
                    ).commit()
                }
            }

            //boolean - true(이벤트 인식이 더 좋음!) / false(이벤트 인식이 떨어짐)
            true
        }
    }
}