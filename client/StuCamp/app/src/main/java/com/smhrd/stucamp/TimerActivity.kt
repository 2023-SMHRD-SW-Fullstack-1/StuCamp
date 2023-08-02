package com.smhrd.stucamp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimerActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var timerRunning = false
    private var elapsedTime: Long = 0
    private var position: Int = 0
    private lateinit var backButton: ImageButton
    private lateinit var startStopButton: Button
    private lateinit var resetButton: Button
    private lateinit var textView: TextView
    private lateinit var userEmail: String
    private lateinit var subName: String

    private var startTime: Long = 0
    private var stopTime: Long = 0
    private var differenceTime: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        backButton = findViewById(R.id.back1)
        startStopButton = findViewById(R.id.start_stop_button)
        resetButton = findViewById(R.id.reset_button)
        textView = findViewById(R.id.ResultTime)

        // 유저 spf처리
        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = spf.getString("user", " ")
        val userVO = Gson().fromJson(user, UserVO::class.java)
        val userEmail = userVO.user_email

        val subSpf = getSharedPreferences("SubjectSpf", Context.MODE_PRIVATE)
        val subEditor = subSpf.edit()
        val sub = subSpf.getString("SubjectSpf", " ")
        val subName = sub.toString()


        position = intent.getIntExtra("position", 0)
        elapsedTime = intent.getLongExtra("elapsedTime", 0)

        savedInstanceState?.let {
            elapsedTime = it.getLong("elapsedTime")
            position = it.getInt("position")
        }



        // 오늘 날짜
        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }


        startStopButton.setOnClickListener {
            if (timerRunning) {
                stopTime = System.currentTimeMillis()
                differenceTime = stopTime - startTime +1000
//                elapsedTime += differenceTime

                handler.removeCallbacks(runnable)
                Log.d("MainActivity", "경과 시간: ${differenceTime} ms")


                // save time to server
                val reqQueue = Volley.newRequestQueue(applicationContext)

                val subject_name = subName // 또는 사용자가 선택한 과목 이름 가져오기

//                val ssibal = elapsedTime - ?????

                val recordDetailJson = JSONObject()
                recordDetailJson.put("record_start_date", getCurrentDate())
                recordDetailJson.put("record_end_date", getCurrentDate())
                recordDetailJson.put("record_elapsed_time", differenceTime)
                recordDetailJson.put("record_subject", subject_name)

                val recordJson = JSONObject()
                recordJson.put("record_date", getCurrentDate())
                recordJson.put("user_email", userEmail)
                recordJson.put("record_detail", recordDetailJson)

                val jsonBody = JSONObject()
                jsonBody.put("record", recordJson)

                val requestBody = jsonBody.toString()

                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.50:8888/record/add",
                    {
                            response ->
                        Log.d("Response", response)
                        if (response == "RecordSaveSuccess") {
                            Toast.makeText(applicationContext, "타이머 저장 성공!", Toast.LENGTH_SHORT)
                                .show()
                        } else if (response == "RecordSaveFail") {
                            Toast.makeText(applicationContext, "타이머 저장 실패!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(applicationContext, "에러발생", Toast.LENGTH_SHORT).show()
                        }

                    },
                    { error ->
                        Log.e("Error", "Error while saving time: ${error.localizedMessage}")
                        Toast.makeText(
                            applicationContext,
                            "Error occurred while saving time",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    override fun getBody(): ByteArray {
                        return requestBody.toByteArray(Charsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
                reqQueue.add(request)
                // end

                timerRunning = false
                startStopButton.text = "시작"
            } else {
                startTime = System.currentTimeMillis()

                runnable = createRunnable()
                handler.post(runnable)
                timerRunning = true
                startStopButton.text = "일시 정지"
            }


        } // startStopButton 클릭리스너 끝

        resetButton.setOnClickListener {
            elapsedTime = 0L
            updateTimeUI()
        }

        backButton.setOnClickListener {
//            handler.removeCallbacks(runnable)
//            val intent = Intent().apply {
//                putExtra("elapsedTime", elapsedTime)
//                putExtra("position", position)
//            }
//            Log.d("MainActivity", "Elapsed time value: $elapsedTime, Position: $position")
//            setResult(RESULT_OK, intent)
//            finish()

            // subSpf 데이터 없애기
            subEditor.clear()
            subEditor.commit()

            val it = Intent(this, MainActivity::class.java)
            startActivity(it)

        }
        updateTimeUI()
    }


    // 처음 시간 데려오는 부분
    private fun updateTimeFromDatabase() {
        val requestQueue = Volley.newRequestQueue(applicationContext)

        val request = object : StringRequest(
            Request.Method.POST,
            "http://http://172.30.1.50:8888/record/${userEmail}",
            { response ->
                // 응답(Parsing the response)을 처리하고 업데이트된 시간을 표시하세요
                val jsonObject = JSONObject(response)
                elapsedTime = jsonObject.getLong("elapsed_time")
                updateTimeUI()
            },
            { error ->
                Log.e("Error", "Error while getting saved time: ${error.localizedMessage}")
                Toast.makeText(
                    applicationContext,
                    "Error occurred while getting saved time",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // 사용자 이메일과 과목 이름을 매개변수로 전달
                params["user_email"] = userEmail
                params["subject_name"] = subName
                return params
            }
        }
        requestQueue.add(request)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("elapsedTime", elapsedTime)
        outState.putInt("position", position)
    }

    private fun createRunnable(): Runnable {
        return Runnable {
            elapsedTime += 1000
            updateTimeUI()
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun updateTimeUI() {
        val hours = (elapsedTime / 3600000)
        val minutes = (elapsedTime % 3600000) / 60000
        val seconds = (elapsedTime % 60000) / 1000

        textView.text = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
    }
}
