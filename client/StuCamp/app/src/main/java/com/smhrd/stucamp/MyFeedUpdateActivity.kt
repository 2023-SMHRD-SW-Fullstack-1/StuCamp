package com.smhrd.stucamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MyFeedUpdateActivity : AppCompatActivity() {

    lateinit var btnBack : ImageButton
    lateinit var ibGallery : ImageButton
    lateinit var ibCamera : ImageButton
    lateinit var ivUpload : ImageView
    lateinit var etContent : EditText
    lateinit var btnWrite : Button

    lateinit var reqQueue : RequestQueue

    lateinit var encodeImgString : String

    val STORAGE_CODE = 1000

    //카메라 촬영
    val REQUEST_IMAGE_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feed_update)

        btnBack = findViewById(R.id.btnBack)
        ibGallery = findViewById(R.id.ibGallery)
        ibCamera = findViewById(R.id.ibCamera)
        ivUpload = findViewById(R.id.ivUpload)
        btnWrite = findViewById(R.id.btnWrite)
        etContent = findViewById(R.id.etContent)

        reqQueue = Volley.newRequestQueue(this@MyFeedUpdateActivity)

        //이전화면으로 돌아가기(Main)
        btnBack.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //갤러리 버튼 클릭
        ibGallery.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            // startActivityForResult : 다른 액티비티가 시작된 후 그 액티비티에 결과를 다시 가져오고 싶을 때!
            startActivityForResult(intent, STORAGE_CODE)
        }

    }
}