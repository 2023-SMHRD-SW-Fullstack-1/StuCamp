// 신지훈
package com.smhrd.stucamp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import java.util.Locale

class Fragment2 : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var timerRunning = false
    private var elapsedTime: Long = 0 // ms

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        val calendarbtn : Button = view.findViewById(R.id.calendarbtn)




        calendarbtn.setOnClickListener{
            var next: Intent = Intent(requireActivity(), CalendarActivity::class.java)
            startActivity(next)


        }

        val startStopButton: Button = view.findViewById(R.id.start_stop_button)
        startStopButton.setOnClickListener {
            if (timerRunning) {
                handler.removeCallbacks(runnable) // 스탑워치 멈춤
                Log.d("MainActivity", "경과 시간: ${elapsedTime}  ms")
                timerRunning = false
            } else {

                runnable = createRunnable()
                handler.post(runnable) // 스탑워치 시작

                timerRunning= true
            }
        }

        val resetButton : Button=view.findViewById(R.id.reset_button)
        resetButton.setOnClickListener{
            elapsedTime=0L
            updateTimeUI()
        }

        updateTimeUI()
        return view
    }

    private fun createRunnable(): Runnable{
        return Runnable{
            updateTimeUI()
            elapsedTime += 1000
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun updateTimeUI(){
        val textView: TextView = view?.findViewById(R.id.textView) ?: return

        val hours=(elapsedTime/3600000)
        val minutes=(elapsedTime%3600000)/60000
        val seconds=(elapsedTime%60000)/1000

        textView.text=String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
    }
}
