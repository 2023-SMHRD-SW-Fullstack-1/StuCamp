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

class MemDeleteActivity : AppCompatActivity() {

    lateinit var etDeleteEmail : EditText
    lateinit var etDeletePw : EditText
    lateinit var etDeletePwCheck : EditText
    lateinit var btnDelete : Button

    lateinit var reqQueue : RequestQueue


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
        val user  = spf.getString("user", " ")
        Log.d("에러", "user"+user.toString())
        val userVO = Gson().fromJson(user, UserVO::class.java)

        // Email text 설정 (사용자 입력 불가)
        val userEmail = userVO.user_email
        etDeleteEmail.keyListener = null
        etDeleteEmail.setText(userEmail.toString())

        val userNick = userVO.user_nickname

        btnDelete.setOnClickListener{
            val inputPw = etDeletePw.text.toString()
            val inputPwCheck = etDeletePwCheck.text.toString()

            if(inputPw == inputPwCheck){
                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.25:8888/user/delete",
                    {
                            response ->
                        Log.d("response", response)

                        if(response == "fail") {
                            Toast.makeText(this, "이메일 또는 비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show()
                        }

                        else {
                            val result = JSONArray(response)
                            Log.d("result", result.toString())
                            val user  = result.getJSONObject(0)
                            Log.d("user", user.toString())
                            // Editor 생성
                            val editor = spf.edit()
                            // editor를 통해 로그인한 회원의 정보 저장
                            editor.putString("user", user.toString())
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
                        val user : UserVO = UserVO(userEmail, inputPw, userNick)
                        params.put("user", Gson().toJson(user))
                        Log.d("params", user.toString())

                        return params
                    }
                }
                reqQueue.add(request)
            } // 비밀번호 확인 일치 끝
            else{
                Toast.makeText(this,"비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
            } // 비밀번호 확인 불일치 끝

        }

    }
}