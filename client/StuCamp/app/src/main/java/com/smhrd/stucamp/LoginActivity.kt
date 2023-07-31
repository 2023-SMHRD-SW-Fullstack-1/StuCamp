package com.smhrd.stucamp
// 이지희

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    lateinit var etLoginEmail : EditText
    lateinit var etLoginPw : EditText
    lateinit var btnLogin : Button

    lateinit var reqQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPw = findViewById(R.id.etLoginPw)
        btnLogin = findViewById(R.id.btnLogin)

        reqQueue = Volley.newRequestQueue(this@LoginActivity)

        btnLogin.setOnClickListener{
            val inputEmail = etLoginEmail.text.toString()
            val inputPw = etLoginPw.text.toString()

            Log.d("inputEmail" , inputEmail)
            Log.d("inputPw" , inputPw)


//                val request = object : StringRequest(
//                    Request.Method.POST,
//                    "",
//                    {
//                        response ->
//                        Log.d("response", response)
//                    },
//                    {
//                        error ->
//                        Log.d("error", error.toString())
//                    }
//                ){
//                    override fun getParams(): MutableMap<String, String>? {
//                        val params : MutableMap<String, String> = HashMap()
//                        val mv = MemberVO(inputEmail, inputPw, null)
//                        params.put("Member", Gson().toJson(mv))
//
//                        return params
//                    }
//                }
//                reqQueue.add(request)

            // 런처 보내기


        }
    }
}