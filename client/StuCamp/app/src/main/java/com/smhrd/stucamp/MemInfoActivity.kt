package com.smhrd.stucamp
// 이지희
// **url 수정 필요!

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
import com.smhrd.stucamp.VO.UserVO

class MemInfoActivity : AppCompatActivity() {

    lateinit var etInfoEmail: EditText
    lateinit var etInfoPw: EditText
    lateinit var etInfoPwCheck: EditText
    lateinit var etInfoNick: EditText
    lateinit var btnLogout : Button
    lateinit var btnSetInfo: Button
    lateinit var btnToDelete : Button

    lateinit var reqQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_info)

        etInfoEmail = findViewById(R.id.etInfoEmail)
        etInfoPw = findViewById(R.id.etInfoPw)
        etInfoPwCheck = findViewById(R.id.etInfoPwCheck)
        etInfoNick = findViewById(R.id.etInfoNick)
        btnLogout = findViewById(R.id.btnLogout)
        btnSetInfo = findViewById(R.id.btnSetInfo)
        btnToDelete = findViewById(R.id.btnToDelete)

        reqQueue = Volley.newRequestQueue(this@MemInfoActivity)

        // spf 처리
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = spf.getString("user", " ")
        val editor = spf.edit()
        val userVO = Gson().fromJson(user, UserVO::class.java)

        val userEmail = userVO.user_email
        val userNick = userVO.user_nickname

        // Email text 설정 (사용자 입력 불가)
        etInfoEmail.keyListener = null
        etInfoEmail.setText(userEmail.toString())
        etInfoNick.setText(userNick.toString())

        btnSetInfo.setOnClickListener {
            val inputPw = etInfoPw.text.toString()
            val inputPwCheck = etInfoPwCheck.text.toString()
            val inputNick = etInfoNick.text.toString()


            if (inputPw == inputPwCheck) {
                val request = object : StringRequest(
                    Request.Method.PUT,
                    "http://172.30.1.22:8888/user/update",
                    { response ->
                        Log.d("update response", response)

                        val result = response

                        if (result == "1") {

                            val updatedUser = UserVO(userEmail, inputPw, inputNick)
                            val updatedUserJson = Gson().toJson(updatedUser)

                            // editor를 통해 로그인한 회원의 정보 저장
                            editor.putString("user", updatedUserJson)
                            editor.commit()

                            Toast.makeText(this, "수정 성공!", Toast.LENGTH_LONG).show()

                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d("response", response)
                            Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }

                    },
                    { error ->
                        Log.d("error", error.toString())
                        Toast.makeText(this, "에러발생!", Toast.LENGTH_LONG).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        val user: UserVO = UserVO(userEmail, inputPw, inputNick)
                        params.put("updateUser", Gson().toJson(user))           // 이 부분 중요
                        Log.d("updateUser", user.toString())

                        return params
                    }
                }
                reqQueue.add(request)
            } // 비밀번호 확인 일치 끝
            else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
            } // 비밀번호 확인 불일치 끝
        } //btnInfo 클릭리스너 끝

        btnToDelete.setOnClickListener{
            val it = Intent(this, MemDeleteActivity::class.java)
            startActivity(it)
        }

        btnLogout.setOnClickListener{
            editor.remove("user")
            editor.commit()

            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}