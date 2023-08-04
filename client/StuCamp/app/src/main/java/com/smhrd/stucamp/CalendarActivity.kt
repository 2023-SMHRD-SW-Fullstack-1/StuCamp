package com.smhrd.stucamp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.Calendar
import java.util.Locale

// ...



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

    lateinit var pieChart: PieChart

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
        pieChart = findViewById(R.id.pieChart)


        reqQueue = Volley.newRequestQueue(this@CalendarActivity)

        back.setOnClickListener {
            var it_back: Intent = Intent(this, MainActivity::class.java)
            startActivity(it_back)
        }

        // 초기화면 세팅


        val spf = getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = Gson().fromJson(spf.getString("user", " "), UserVO::class.java)

        fun formatToyyyyMMdd(year: Int, month: Int, dayOfMonth: Int): String {
            return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }

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

        //        fun millisecondsToHHMM(milliseconds: Long): String {
//            val totalSeconds = milliseconds / 1000
//            val hours = totalSeconds / 3600
//            val minutes = (totalSeconds % 3600) / 60
//
//            return String.format("%02d:%02d", hours, minutes)
//        }
        fun millisecondsToHHMM(milliseconds: Long): String {
            val totalSeconds = milliseconds / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        // 초기화면 세팅
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        diaryTextView.visibility = View.VISIBLE

        var todayDate = formatToyyyyMMdd(currentYear, currentMonth, currentDay)
        var userID = user.user_email

        val request = object : StringRequest(Request.Method.GET,
            "http://172.30.1.42:8888/record/${userID}?record_date=${todayDate}",
            { response ->
                tv_detailStudy.text = ""
                diaryTextView.text = "총 공부시간"
                Log.d("response", response)
//                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                if (response == "-1") {
                    // 빈 Date 보냈을시
                    diaryTextView.text = "총 공부시간 : 0분"
                    pieChart.visibility = View.INVISIBLE
                } else {
                    pieChart.visibility = View.VISIBLE

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
                    diaryTextView.text = "총 공부시간 : " + millisecondsToHHMM(sumOfStudy) + "초"

                    val entries = mutableListOf<PieEntry>()

                    var totalStudyTime = 0L
                    for ((subject, time) in sumOfSubject) {
                        totalStudyTime += time
                    }
                    for ((subject, time) in sumOfSubject) {
                        val subjectTimeFormatted = millisecondsToHHMM(time)
//                            tv_detailStudy.text = "${tv_detailStudy.text}$subject : $subjectTimeFormatted 분\n"
                        val percentage = (time.toDouble() / totalStudyTime.toDouble()) * 100
//                            entries.add(PieEntry(time.toFloat(), "$subject : $subjectTimeFormatted 분"))
                        entries.add(
                            PieEntry(
                                percentage.toFloat(),
                                "$subject : $subjectTimeFormatted 초"
                            )
                        )
                    }

                    val dataSet = PieDataSet(entries, "")
                    dataSet.sliceSpace = 3f
                    dataSet.selectionShift = 5f
                    dataSet.valueTextColor = Color.BLACK
                    dataSet.valueTextSize = 16f


                    // 색상 설정
                    dataSet.colors = mutableListOf(
//                        Color.BLUE,
//                        Color.GREEN,
//                        Color.RED,
//                        Color.YELLOW,
//                        Color.CYAN,
                        Color.rgb(239,154,154),
                        Color.rgb(179,157,219),
                        Color.rgb(144,202,249),
                        Color.rgb(128,203,196),
                        Color.rgb(230,238,156),
                        Color.rgb(255,204,128),
                    )

                    val legend = pieChart.legend
                    legend.orientation = Legend.LegendOrientation.VERTICAL
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    legend.textSize = 14f

                    val percentFormatter = PercentFormatter(pieChart)
                    dataSet.valueFormatter = percentFormatter




                    val data = PieData(dataSet)
                    data.setValueFormatter(percentFormatter)
                    pieChart.data = data
                    pieChart.setUsePercentValues(true)
                    pieChart.setDrawEntryLabels(false)
                    pieChart.description.isEnabled = false

                    // 애니메이션
                    pieChart.animateY(1000)

                    pieChart.invalidate()



                }


            },
            { error ->
                Log.d("error", error.toString())
                Toast.makeText(this, "에러발생!", Toast.LENGTH_LONG).show()
            }) {

        }
        reqQueue.add(request)



        // 데이트 체인지 이벤트
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE

            var selectedDate = formatToyyyyMMdd(year, month, dayOfMonth)
            var userID = user.user_email

//            diaryTextView.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
//            checkDay(year, month, dayOfMonth, userID)


            val request = object : StringRequest(Request.Method.GET,
                "http://172.30.1.42:8888/record/${userID}?record_date=${selectedDate}",
                { response ->
                    tv_detailStudy.text = ""
                    diaryTextView.text = "총 공부시간"
                    Log.d("response", response)
//                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                    if (response == "-1") {
                        // 빈 Date 보냈을시
                        diaryTextView.text = "총 공부시간 : 0분"
                        pieChart.visibility = View.INVISIBLE
                    } else {
                        pieChart.visibility = View.VISIBLE

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
                        diaryTextView.text = "총 공부시간 : " + millisecondsToHHMM(sumOfStudy) + "초"

//                        for ((subject, time) in sumOfSubject) {
//                            val subjectTimeFormatted = millisecondsToHHMM(time)
//                            tv_detailStudy.text = "${tv_detailStudy.text}$subject : $subjectTimeFormatted 분\n"
//                        }

                        val entries = mutableListOf<PieEntry>()

                        var totalStudyTime = 0L
                        for ((subject, time) in sumOfSubject) {
                            totalStudyTime += time
                        }
                        for ((subject, time) in sumOfSubject) {
                            val subjectTimeFormatted = millisecondsToHHMM(time)
//                            tv_detailStudy.text = "${tv_detailStudy.text}$subject : $subjectTimeFormatted 분\n"
                            val percentage = (time.toDouble() / totalStudyTime.toDouble()) * 100
//                            entries.add(PieEntry(time.toFloat(), "$subject : $subjectTimeFormatted 분"))
                            entries.add(
                                PieEntry(
                                    percentage.toFloat(),
                                    "$subject : $subjectTimeFormatted 초"
                                )
                            )
                        }

                        val dataSet = PieDataSet(entries, "")
                        dataSet.sliceSpace = 3f
                        dataSet.selectionShift = 5f
                        dataSet.valueTextColor = Color.BLACK
                        dataSet.valueTextSize = 16f


                        // 색상 설정
                        dataSet.colors = mutableListOf(
                            Color.BLUE,
                            Color.GREEN,
                            Color.RED,
                            Color.YELLOW,
                            Color.CYAN
                        )

                        val legend = pieChart.legend
                        legend.orientation = Legend.LegendOrientation.VERTICAL
                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                        legend.textSize = 14f

//                        dataSet.valueFormatter = CustomPercentFormatter(pieChart)
//                        dataSet.valueFormatter = PercentFormatter(pieChart)
                        val percentFormatter = PercentFormatter(pieChart)
                        dataSet.valueFormatter = percentFormatter




                        val data = PieData(dataSet)
                        data.setValueFormatter(percentFormatter)
                        pieChart.data = data
                        pieChart.setUsePercentValues(true)
                        pieChart.setDrawEntryLabels(false)
                        pieChart.description.isEnabled = false

                        // 애니메이션
                        pieChart.animateY(1000)

                        pieChart.invalidate()



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