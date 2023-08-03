package com.smhrd.stucamp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity.RESULT_OK
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.stucamp.VO.UserVO
import org.json.JSONObject
import java.util.Date
import java.util.Locale

class Fragment2 : Fragment() {
    private lateinit var adapter: CustomAdapter
    private var totalTime: Long = 0L
    private lateinit var resultTimeTextView: TextView
    private var timerData: ArrayList<Long> = ArrayList()
    lateinit var reqQueue : RequestQueue
    private val recordData: HashMap<String, Long> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        // 유저 spf처리
        val userSpf = requireActivity().getSharedPreferences("mySPF", Context.MODE_PRIVATE)
        val user = userSpf.getString("user", " ")
        val userVO = Gson().fromJson(user, UserVO::class.java)
        val userEmail = userVO.user_email

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        reqQueue = Volley.newRequestQueue(requireActivity())

        val request = object : StringRequest(
            Request.Method.GET,

            "http://172.30.1.22:8888/record/${userEmail}?record_date=${today}",
            { response ->
                Log.d("response", response.toString())
                val result = JSONObject(response)

                val abc = result.getJSONArray("recordDetails")
                val recordDetails = abc.getJSONArray(0)



                for (i in 0 until recordDetails.length()) {
                    val recordDetail = recordDetails.getJSONObject(i)

                    val recordSubject = recordDetail.getString("record_subject")
                    val recordElapsedTime = recordDetail.getLong("record_elapsed_time")
                    recordData[recordSubject] = recordData.getOrDefault(recordSubject, 0L) + recordElapsedTime
                }

                for ((recordSubject, recordElapsedTime) in recordData) {
                    val formattedElapsedTime = formatElapsedTime(recordElapsedTime)
                    adapter.addItem(recordSubject, formattedElapsedTime)
                    timerData.add(recordElapsedTime)
                    totalTime += recordElapsedTime
                }
                updateResultTime(totalTime)
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.d("error", error.toString())
            }
        ) {}
        reqQueue.add(request)



        // 과목 추가 액티비티로 이동
        val addButton: Button = view.findViewById(R.id.add)
        addButton.setOnClickListener {
            val intent = Intent(activity, NameInputActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // 캘린더 액티비티로 이동
        val calendarbtn: Button = view.findViewById(R.id.calendarbtn)
        calendarbtn.setOnClickListener {
            var next: Intent = Intent(requireActivity(), CalendarActivity::class.java).apply {
                putExtra("totalTime", formatElapsedTime(totalTime))
            }
            startActivity(next)
        }


        // 리스트뷰 어댑터
        val listView: ListView = view.findViewById(R.id.listView)
        adapter = CustomAdapter(requireContext())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val currentSubject = adapter.getItem(position)?.first ?: return@setOnItemClickListener
            val intent = Intent(activity, TimerActivity::class.java).apply {
                putExtra("position", position)
//                putExtra("elapsedTime", timerData[position])
                putExtra("elapsedTime", recordData[currentSubject])
            }
            startActivityForResult(intent, 2)

            // 항목의 이름 값을 가져옵니다.
            val currentName = adapter.getItem(position)?.first

            // SharedPreferences에 이름 값을 저장합니다.
            val sharedPreferences = requireActivity().getSharedPreferences("SubjectSpf", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("SubjectSpf", currentName)
            Log.d("SubjectSpf", sharedPreferences.toString())
            editor.commit()
        }

        val subjectsPreferences = requireActivity().getSharedPreferences("Subjects", Context.MODE_PRIVATE)
        val subjectsIds = subjectsPreferences.getStringSet("SubjectsIds", mutableSetOf<String>()) ?: mutableSetOf()

        // 저장된 과목 데이터를 불러와 adapter에 추가합니다.
        for (subjectId in subjectsIds) {
            val subject = subjectsPreferences.getString(subjectId, "")
            val elapsedTime = subjectsPreferences.getLong("${subjectId}_time", 0L)
//            adapter.addItem(subject!!, formatElapsedTime(elapsedTime))
            timerData.add(elapsedTime)
            totalTime += elapsedTime // 여기에서 이전에 측정한 시간을 합산합니다.
        }

        return view
    } // view끝
    private fun isSubjectDuplicated(subject: String): Boolean {
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i)?.first == subject) {
                return true
            }
        }
        return false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // 과목명 데려오기
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name") ?: ""

            if (!isSubjectDuplicated(name)) {
                val newSubjectId = "subject_${System.currentTimeMillis()}"

                val subjectsPreferences = requireActivity().getSharedPreferences("Subjects", Context.MODE_PRIVATE)
                val subjectsIds = subjectsPreferences.getStringSet("SubjectsIds", mutableSetOf<String>()) ?: mutableSetOf()
                val editor = subjectsPreferences.edit()

                editor.putString(newSubjectId, name)
                subjectsIds.add(newSubjectId)
                editor.putStringSet("SubjectsIds", subjectsIds)
                editor.apply()

                adapter.addItem(name)
                timerData.add(0L)
            } else {
                Toast.makeText(requireContext(), "과목이 이미 존재합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 시간 데려오기
        if (requestCode == 2 && resultCode == RESULT_OK) {
            val elapsedTime = data?.getLongExtra("elapsedTime", 0) ?: 0
            val position = data?.getIntExtra("position", 0)!!
            val subjectId = adapter.getItem(position)?.first

            totalTime +=  elapsedTime

            updateResultTime(totalTime)

            val formattedElapsedTime = formatElapsedTime(elapsedTime)

            adapter.updateTime(position, formattedElapsedTime)
            timerData[position] = elapsedTime

            val sharedPreferences = requireActivity().getSharedPreferences("Subjects", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong("${subjectId}_time", elapsedTime)
            editor.apply()
        }
    } //onActivityResult끝

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultTimeTextView = view.findViewById(R.id.ResultTime)
        updateResultTime(totalTime)
    }// 시분초 계산 후 화면에 띄우기

    private fun updateResultTime(totalTime: Long) {
        val formattedTotalTime = formatElapsedTime(totalTime)
        resultTimeTextView.text = formattedTotalTime
    }// 시분초 계산 후 return 끝

    private fun formatElapsedTime(elapsedTime: Long): String {
        val hours = (elapsedTime / 3600000)
        val minutes = (elapsedTime % 3600000) / 60000
        val seconds = (elapsedTime % 60000) / 1000
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }// 시분초 계산 후 return 끝
} // class 끝



// ------------------------------------------

class CustomAdapter(context: Context) : ArrayAdapter<Pair<String, String>>(context, R.layout.list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val name = this.getItem(position)?.first ?: ""
        val time = this.getItem(position)?.second ?: ""

        val nameTextView: TextView = listItem!!.findViewById(R.id.name_textView)
        val timeTextView: TextView = listItem.findViewById(R.id.time_textView)

        nameTextView.text = name
        timeTextView.text = time

        return listItem
    }

    fun addItem(name: String, time: String = "00:00:00") {
        this.add(Pair(name, time))
    }

    fun updateTime(position: Int, time: String) {
        val item = this.getItem(position)
        item?.let {
            this.remove(item)
            this.insert(Pair(item.first, time), position)
        }
    }

} // CustomAdapter(과목 추가) 끝
