package com.smhrd.stucamp
// 이지희

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


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
            val inputPw = etSignInPw.text.toString()
            val inputPwCheck = etSignInPwCheck.text.toString()
            val inputNick = etSignInNick.text.toString()

//            if(inputPw==inputPwCheck){
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
//                        val mv = MemberVO(inputEmail, inputPw, inputNick)
//                        params.put("Member", Gson().toJson(mv))
//
//                        return params
//                    }
//                }
//                reqQueue.add(request)
//            } //비밀번호 일치
//            else{
//             Toast.makeText(this,"비밀번호가 일치하지 않습니다",Toast.LENGTH_LONG).show()
//            } // 비밀번호 불일치


        }


    }
}