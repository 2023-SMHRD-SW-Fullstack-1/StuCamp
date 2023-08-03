package com.smhrd.stucamp
// 이지희
// **url 수정 필요!

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject
import java.net.URL

class LoginActivity : AppCompatActivity() {

    lateinit var etLoginEmail : EditText
    lateinit var etLoginPw : EditText
    lateinit var btnLogin : Button
    lateinit var btnToSignIn : Button

    lateinit var reqQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPw = findViewById(R.id.etLoginPw)
        btnLogin = findViewById(R.id.btnLogin)
        btnToSignIn = findViewById(R.id.btnToSignIn)

        reqQueue = Volley.newRequestQueue(this@LoginActivity)


        // SharedPreference 생성
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = spf.getString("user", " ")

        btnLogin.setOnClickListener{
            val inputEmail = etLoginEmail.text.toString()
            val inputPassword = etLoginPw.text.toString()

            val url = URL("http://172.30.1.22:8888/user/login")


            Log.d("inputEmail" , inputEmail)
            Log.d("inputPw" , inputPassword)


            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.25:8888/user/login",
                {
                        response ->
                    Log.d("response", response)
//                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()

                    if(response.equals("-1")) {
                        Toast.makeText(this, "아이디나 비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
                    }else{
                        val user = JSONObject(response)
                        // Editor 생성
                        val editor = spf.edit()
                        // editor를 통해 로그인한 회원의 정보 저장
                        editor.putString("user", user.toString())
                        editor.commit()

                        // MainActivity로 전환 (Intent)joinUser
                        val it = Intent(this, MainActivity::class.java)
                        startActivity(it)
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
                    val user : UserVO = UserVO(inputEmail, inputPassword, null)
                    params.put("loginUser", Gson().toJson(user))

                    return params
                }
            }
            reqQueue.add(request)
        } // btnLogin 클릭리스너 끝

        btnToSignIn.setOnClickListener{
            val it = Intent(this, SignInActivity::class.java)
            startActivity(it)
        } // btnToSignIn 클릭리스너 끝


    }
}