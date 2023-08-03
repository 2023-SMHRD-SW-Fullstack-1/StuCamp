package com.smhrd.stucamp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import java.io.FileInputStream
import java.io.FileOutputStream

import android.view.View
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.smhrd.stucamp.VO.RecordDetail
import com.smhrd.stucamp.VO.RecordDetailList
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.Locale


class CalendarActivity : AppCompatActivity() {
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var calendarView: CalendarView

    lateinit var diaryTextView: TextView
    lateinit var title: TextView
    lateinit var contextEditText: EditText
    lateinit var back: ImageButton
    lateinit var tv_detailStudy: TextView

    lateinit var reqQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        // UI값 생성
        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)

        title = findViewById(R.id.title)
        back = findViewById(R.id.back)
        tv_detailStudy = findViewById(R.id.tv_detailStudy)

        reqQueue = Volley.newRequestQueue(this@CalendarActivity)

        back.setOnClickListener {
            var it_back: Intent = Intent(this, MainActivity::class.java)
            startActivity(it_back)
        }

        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", " "), UserVO::class.java)

        fun formatToyyyyMMdd(year: Int, month: Int, dayOfMonth: Int): String {
            return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }

//        fun parseJson(jsonData: String): RecordDetailList {
//            // JSON 문자열을 파싱하여 RecordEntity 객체 생성
//            val jsonObject = JsonParser.parseString(jsonData).asJsonObject
//            val recordDetailsJson = jsonObject.getAsJsonArray("recordDetails")
//
//            val recordDetailsLists = mutableListOf<List<RecordDetail>>()
//
//            for (recordListJson in recordDetailsJson) {
//                val recordList = mutableListOf<RecordDetail>()
//                val recordListArray = recordListJson.asJsonArray
//
//                for (recordDetailJson in recordListArray) {
//                    val recordDetail = recordDetailJson.asJsonObject
//                    val startDate = recordDetail.get("record_start_date").asString
//                    val endDate = recordDetail.get("record_end_date").asString
//                    val elapsedTime = recordDetail.get("record_elapsed_time").asLong
//                    val subject = recordDetail.get("record_subject").asString
//
//                    recordList.add(RecordDetail(startDate, endDate, elapsedTime, subject))
//                }
//
//                recordDetailsLists.add(recordList)
//            }
//
//            return RecordDetailList(recordDetailsLists)
//        }

        fun parseJson(jsonData: String): RecordDetailList {
            val jsonObject = JsonParser.parseString(jsonData).asJsonObject
            val recordDetailsJson = jsonObject.getAsJsonArray("recordDetails")

            val recordDetailsLists = mutableListOf<List<RecordDetail>>()
            val sumOfSubject = mutableMapOf<String, Long>()

            for (recordListJson in recordDetailsJson) {
                val recordList = mutableListOf<RecordDetail>()
                val recordListArray = recordListJson.asJsonArray

                for (recordDetailJson in recordListArray) {
                    val recordDetail = recordDetailJson.asJsonObject
                    val startDate = recordDetail.get("record_start_date").asString
                    val endDate = recordDetail.get("record_end_date").asString
                    val elapsedTime = recordDetail.get("record_elapsed_time").asLong
                    val subject = recordDetail.get("record_subject").asString

                    recordList.add(RecordDetail(startDate, endDate, elapsedTime, subject))

                    // Calculate total time for each subject
                    sumOfSubject[subject] = sumOfSubject.getOrDefault(subject, 0) + elapsedTime
                }

                recordDetailsLists.add(recordList)
            }

            return RecordDetailList(recordDetailsLists, sumOfSubject)
        }

        fun millisecondsToHHMM(milliseconds: Long): String {
            val totalSeconds = milliseconds / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60

            return String.format("%02d 시간 %02d", hours, minutes)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE

            var selectedDate = formatToyyyyMMdd(year, month, dayOfMonth)
            var userID = user.user_email

//            diaryTextView.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
//            checkDay(year, month, dayOfMonth, userID)


            val request = object : StringRequest(Request.Method.GET,
                "http://172.30.1.52:8888/record/${userID}?record_date=${selectedDate}",
                { response ->
                    tv_detailStudy.text = ""
                    diaryTextView.text = "총 공부시간"
                    Log.d("response", response)
//                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                    if (response == "-1") {
                        // 빈 Date 보냈을시
                        diaryTextView.text = "총 공부시간 : 0분"
                    } else {
                        var result = response.trimIndent()
                        Log.d("response222", result)

                        val jsonDataObj = parseJson(result)

                        var sumOfStudy: Long = 0
                        val sumOfSubject = mutableMapOf<String, Long>()

                        for (recordList in jsonDataObj.recordDetails) {
                            for (recordDetail in recordList) {
                                sumOfStudy += recordDetail.record_elapsed_time

                                val subject = recordDetail.record_subject
                                sumOfSubject[subject] = sumOfSubject.getOrDefault(
                                    subject, 0
                                ) + recordDetail.record_elapsed_time
                            }
                        }
                        diaryTextView.text = "총 공부시간 : " + millisecondsToHHMM(sumOfStudy) + "분"
                        for ((subject, time) in sumOfSubject) {
                            val subjectTimeFormatted = millisecondsToHHMM(time)
                            tv_detailStudy.text = "${tv_detailStudy.text}$subject : $subjectTimeFormatted 분\n"
                        }
                    }


                },
                { error ->
                    Log.d("error", error.toString())
                    Toast.makeText(this, "에러발생!", Toast.LENGTH_LONG).show()
                }) {
//                override fun getParams(): MutableMap<String, String>? {
//                    val params : MutableMap<String, String> = HashMap()
//                    val user : UserVO = UserVO(inputEmail, inputPassword, null)
//                    params.put("loginUser", Gson().toJson(user))
//
//                    return params
//                }
            }
            reqQueue.add(request)

        }


    }


    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름설정


    }


    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {

    }


    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {

    }
}