package com.smhrd.stucamp
// 이지희

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import java.net.HttpURLConnection
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
        val test = spf.getString("user", " ")
        Log.d("userState", test.toString())

        btnLogin.setOnClickListener{
            val inputEmail = etLoginEmail.text.toString()
            val inputPassword = etLoginPw.text.toString()

            val url = URL("http://172.30.1.25:8888/user/login")

            val connection = url.openConnection() as HttpURLConnection
            connection.run {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }

// 결과 얻기
            val status = connection.responseCode // HTTP 상태 코드 가져오기


            Log.d("inputEmail" , inputEmail)
            Log.d("inputPw" , inputPassword)


            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.25:8888/user/login",
                {
                        response ->
                    Log.d("response", response)

                    if(response=="Fail") {
                        Toast.makeText(this, "이메일 또는 비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else if(response=="Success") {
//                        val result = JSONArray(response)
//                        Log.d("result", result.toString())
//                        val user  = result.getJSONObject(0)
//                        Log.d("user", user.toString())
                        // Editor 생성
                        val editor = spf.edit()
                        // editor를 통해 로그인한 회원의 정보 저장
                        editor.putString("user", inputEmail)
                        editor.commit()

                        // MainActivity로 전환 (Intent)joinUser
                        val it = Intent(this, MainActivity::class.java)
                        startActivity(it)
                    } else{
                        Log.d("response 오류", response)
                        Toast.makeText(this, "로그인 실패!", Toast.LENGTH_LONG).show()
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
//                    val user : UserVO = UserVO(inputEmail, inputPassword, null)
//                    params.put("user", Gson().toJson(user))
//                    Log.d("params", user.toString())

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