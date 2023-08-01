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


class SignInActivity : AppCompatActivity() {

    lateinit var etSignInEmail : EditText
    lateinit var etSignInPw : EditText
    lateinit var etSignInPwCheck : EditText
    lateinit var etSignInNick : EditText
    lateinit var btnSignIn : Button

    lateinit var reqQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        etSignInEmail = findViewById(R.id.etSignInEmail)
        etSignInPw = findViewById(R.id.etSignInPw)
        etSignInPwCheck = findViewById(R.id.etSignInPwCheck)
        etSignInNick = findViewById(R.id.etSignInNick)
        btnSignIn = findViewById(R.id.btnSignIn)

        reqQueue = Volley.newRequestQueue(this@SignInActivity)

        btnSignIn.setOnClickListener{
            val inputEmail = etSignInEmail.text.toString()
            val inputPassword = etSignInPw.text.toString()
            val inputPwCheck = etSignInPwCheck.text.toString()
            val InputNickname = etSignInNick.text.toString()

            reqQueue = Volley.newRequestQueue(this@SignInActivity)

            // SharedPreference 생성
            val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
            val test = spf.getString("joinUser", " ")
            Log.d("userState", test.toString())


            if(inputPassword==inputPwCheck){
                 val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.25:8888/user/join",
                    {
                        response ->
                        Log.d("response", response)

                        if(response == "join fail : existed email") {
                            Toast.makeText(this, "이미 존재하는 이메일입니다", Toast.LENGTH_LONG).show()
                        } else if(response == "join fail") {
                            Toast.makeText(this, "가입실패! 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }

                        else {
                            val result = JSONArray(response)
                            Log.d("result", result.toString())
                            val joinUser  = result.getJSONObject(0)
                            Log.d("joinUser", joinUser.toString())
                            // Editor 생성
                            val editor = spf.edit()
                            // editor를 통해 로그인한 회원의 정보 저장
                            editor.putString("joinUser", joinUser.toString())
                            editor.commit()

                            // MainActivity로 전환 (Intent)joinUser
                            val it = Intent(this, LoginActivity::class.java)
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
                        val joinUser : UserVO = UserVO(inputEmail, inputPassword, InputNickname)
                        params.put("joinUser", Gson().toJson(joinUser))
                        Log.d("params", joinUser.toString())

                        return params
                    }
                }
                reqQueue.add(request)
            } // 비밀번호 일치 끝

            else{
             Toast.makeText(this,"비밀번호가 일치하지 않습니다",Toast.LENGTH_LONG).show()
            } // 비밀번호 불일치 끝
        } // btn
    }
}