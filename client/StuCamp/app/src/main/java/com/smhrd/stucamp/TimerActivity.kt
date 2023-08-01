package com.smhrd.stucamp import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale class TimerActivity : AppCompatActivity() {
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

        startStopButton.setOnClickListener {
            if (timerRunning) {
                handler.removeCallbacks(runnable)
                Log.d("MainActivity", "경과 시간: ${elapsedTime} ms")
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

//        if (timerData.size > position) {
//            timerData[position] = elapsedTime
//        } else {
//            timerData.add(position, elapsedTime)
//        }
    }
}