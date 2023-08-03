package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.FeedDelVO
import com.smhrd.stucamp.VO.FeedUpdateVO
import com.smhrd.stucamp.VO.UserVO
import java.io.ByteArrayOutputStream

class MyFeedUpdateActivity : AppCompatActivity() {

    lateinit var btnBack : ImageButton
    lateinit var ibGallery : ImageButton
    lateinit var ibCamera : ImageButton
    lateinit var ivUpload : ImageView
    lateinit var etContent : EditText
    lateinit var btnWrite : Button

    lateinit var reqQueue : RequestQueue

//    lateinit var encodeImgString : String
    var encodeImgString : String ? = null

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

        //카메라 버튼 클릭
        ibCamera.setOnClickListener(){
            takePicture()
        }

        //SharedPreference 생성
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", ""), UserVO::class.java)
        val user_email = user.user_email

        //기존 정보 불러오기
        val intent = intent
        val feed_id = intent.getIntExtra("feed_id", 0)
        etContent.setText(intent.getStringExtra("feed_content"))
        //이미지 변환
        val feed_img = intent.getStringExtra("feed_img")
//        Log.d("feed img : ", feed_img.toString())
        val imageBytes = Base64.decode(feed_img, 0)
        val feed_img2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ivUpload.setImageBitmap(feed_img2)

        //피드 수정하기
        btnWrite.setOnClickListener {
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.22:8888/feed/update",
                {
                        response ->
                    Log.d("response", response)
//                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                    if(response == "1"){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                    if(response.equals("0")) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                    }else{

                    }
                },
                {
                        error ->
                    Log.d("error", error.toString())
                    Toast.makeText(this, "에러발생!", Toast.LENGTH_LONG).show()
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params : MutableMap<String, String> = HashMap()
                    val updateFeed : FeedUpdateVO
                    if(encodeImgString != null){
//                        Log.d("aaaaaaaaaaa", encodeImgString)
                        updateFeed = FeedUpdateVO(etContent.text.toString(), encodeImgString!!, user_email, feed_id)
                    }else{
                        updateFeed = FeedUpdateVO(etContent.text.toString(), feed_img.toString(), user_email, feed_id)
                    }
                    params.put("updateFeed", Gson().toJson(updateFeed))

                    return params
                }
            }
            reqQueue.add(request)
        }

    }

    // 카메라 버튼 눌러서 사진찍기
    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { imageTakeIntent ->
            imageTakeIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            STORAGE_CODE ->{
                //image uri 가져오기
                val selectedImgUri = data?.data

                Log.d("selectedImgUri", selectedImgUri.toString())

                if(selectedImgUri != null){
                    //uri -> bitmap 형태로 변환
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImgUri)
                    ivUpload.setImageBitmap(bitmap)

                    val options = BitmapFactory.Options()
                    options.inSampleSize = 2 // 4개의 픽셀 -> 1개의 픽셀 => 1/4 크기로 변환

                    // filter => true(선명하게) / false(흐릿하게)
                    val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)

                    encodeBitmapImg(resized)
                }
            }
        }

        // 카메라 촬영 결과물 가져오기
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            ivUpload.setImageBitmap(imageBitmap)
        }
    }

    //bitmap -> String (Base64)
    private fun encodeBitmapImg(bitmap: Bitmap){

        //ByteArray 생성할 수 있는 stream
        val byteArrayOutputStream = ByteArrayOutputStream()
        //받아온 bitmap 압축하기
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        //img => array
        val byteOfImg = byteArrayOutputStream.toByteArray()

        //encoding : ByteArray -> String(Base64 사용해서 encoding)
        encodeImgString = Base64.encodeToString(byteOfImg, Base64.DEFAULT)
    }
}

