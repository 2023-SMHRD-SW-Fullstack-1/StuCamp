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

class MemDeleteActivity : AppCompatActivity() {

    lateinit var etDeleteEmail: EditText
    lateinit var etDeletePw: EditText
    lateinit var etDeletePwCheck: EditText
    lateinit var btnDelete: Button

    lateinit var reqQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_delete)

        etDeleteEmail = findViewById(R.id.etDeleteEmail)
        etDeletePw = findViewById(R.id.etDeletePw)
        etDeletePwCheck = findViewById(R.id.etDeletePwCheck)
        btnDelete = findViewById(R.id.btnDelelte)

        reqQueue = Volley.newRequestQueue(this@MemDeleteActivity)

        // spf 처리
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = spf.getString("user", " ")
        val userVO = Gson().fromJson(user, UserVO::class.java)
        val userEmail = userVO.user_email

        // Email text 설정 (사용자 입력 불가)

        etDeleteEmail.keyListener = null
        etDeleteEmail.setText(userEmail.toString())

        btnDelete.setOnClickListener {
            val inputPw = etDeletePw.text.toString()
            val inputPwCheck = etDeletePwCheck.text.toString()

            if (inputPw == inputPwCheck) {
//                val jsonData = Gson().toJson(UserVO(userEmail, inputPw, null))

                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.52:8888/user/delete",
                    { response ->
                        Log.d("response", response)

                        if (response == "success delete") {

                            // Editor 생성
                            val editor = spf.edit()
                            // editor를 통해 로그인한 회원의 정보 저장
                            editor.clear()
                            editor.commit()
                            Toast.makeText(this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()

                            // MainActivity로 전환 (Intent)joinUser
                            val it = Intent(this, LoginActivity::class.java)
                            startActivity(it)

                        } else {
                            Toast.makeText(this, "이메일 또는 비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG)
                                .show()
                        }

                    },
                    { error ->
                        Log.d("error", error.toString())
                        Toast.makeText(this, "에러발생!", Toast.LENGTH_LONG).show()
                    }) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        val user: UserVO = UserVO(userEmail, inputPw, null)
                        params.put("deleteUser", Gson().toJson(user))

                        return params

                    }

                }
                reqQueue.add(request)
            } // 비밀번호 확인 일치 끝
            else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
            } // 비밀번호 확인 불일치 끝

        }

    }
}