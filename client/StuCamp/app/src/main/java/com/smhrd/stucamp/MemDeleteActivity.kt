package com.smhrd.stucamp
// 이지희
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MemDeleteActivity : AppCompatActivity() {

    lateinit var etDeleteEmail : EditText
    lateinit var etDeletePw : EditText
    lateinit var etDeletePwCheck : EditText
    lateinit var btnDelete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_delete)

        etDeleteEmail = findViewById(R.id.etDeleteEmail)
        etDeletePw = findViewById(R.id.etDeletePw)
        etDeletePwCheck = findViewById(R.id.etDeletePwCheck)
        btnDelete = findViewById(R.id.btnDelelte)

        btnDelete.setOnClickListener{
            val inputEmail = etDeleteEmail.text.toString()
            val inputPw = etDeletePw.text.toString()
            val inputPwCheck = etDeletePwCheck.text.toString()

            Log.d("inputEmail" , inputEmail)
            Log.d("inputPw" , inputPw)
            Log.d("inputPw" , inputPwCheck)

//          if(inputPw == inputPwCheck){
//                    val request = object : StringRequest(
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
//                        val mv = MemberVO(inputEmail, inputPW, null)
//                        params.put("Member", Gson().toJson(mv))
//
//                        return params
//                    }
//                }
//                reqQueue.add(request)
//        }//비밀번호 일치할 시

        }

    }
}