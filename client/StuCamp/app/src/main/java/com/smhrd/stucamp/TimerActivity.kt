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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        backButton = findViewById(R.id.back1)
        startStopButton = findViewById(R.id.start_stop_button)
        resetButton = findViewById(R.id.reset_button)
        textView = findViewById(R.id.ResultTime)

        position = intent.getIntExtra("position", 0)
        elapsedTime = intent.getLongExtra("elapsedTime", 0)

        savedInstanceState?.let {
            elapsedTime = it.getLong("elapsedTime")
            position = it.getInt("position")
        }

        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }


        startStopButton.setOnClickListener {
            if (timerRunning) {
                handler.removeCallbacks(runnable)
                Log.d("MainActivity", "경과 시간: ${elapsedTime} ms")

                // save time to server
                val reqQueue = Volley.newRequestQueue(applicationContext)
                val url = "http://172.30.1.50:8888/record/add"

                val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
                val user_email = spf.getString("user", "a") // user_email

                val jsonBody = JSONObject()
                jsonBody.put("record_date", getCurrentDate()) // 현재 날짜를 yyyy-MM-dd 형식으로 지정해야 합니다.
                jsonBody.put("user_email", user_email)
                val recordDetailJson = JSONObject()
                recordDetailJson.put("record_start_date", getCurrentDate()) // yyyy-MM-dd'T'HH:mm:ss 형식으로 지정해야 합니다.
                recordDetailJson.put("record_end_date", getCurrentDate()) // yyyy-MM-dd'T'HH:mm:ss 형식으로 지정해야 합니다.
                recordDetailJson.put("record_elapsed_time", elapsedTime)
//                recordDetailJson.put("record_subject", "your_subject_name")
                jsonBody.put("record_detail", recordDetailJson)

                val requestBody = jsonBody.toString()

                val request = object : StringRequest(
                    Request.Method.POST,
                    url,
                    {
                            response ->
                        Log.d("Response", "Response from server: $response")
                        Toast.makeText(applicationContext, "Time saved successfully", Toast.LENGTH_SHORT).show()
                    },
                    {
                            error ->
                        Log.e("Error", "Error while saving time: ${error.localizedMessage}")
                        Toast.makeText(applicationContext, "Error occurred while saving time", Toast.LENGTH_SHORT).show()
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
                runnable = createRunnable()
                handler.post(runnable)
                timerRunning = true
                startStopButton.text = "일시 정지"
            }
        }

        resetButton.setOnClickListener {
            elapsedTime = 0L
            updateTimeUI()
        }

        backButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            val intent = Intent().apply {
                putExtra("elapsedTime", elapsedTime)
                putExtra("position", position)
            }
            Log.d("MainActivity", "Elapsed time value: $elapsedTime, Position: $position")
            setResult(RESULT_OK, intent)
            finish()
        }
        updateTimeUI()
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
