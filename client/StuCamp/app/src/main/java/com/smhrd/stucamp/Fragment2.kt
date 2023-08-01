package com.smhrd.stucamp import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.util.Locale class Fragment2 : Fragment() {
    private lateinit var adapter: CustomAdapter
    private var totalTime: Long = 0L
    private lateinit var resultTimeTextView: TextView
    private var timerData: ArrayList<Long> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        val addButton: Button = view.findViewById(R.id.add)
        addButton.setOnClickListener {
            val intent = Intent(activity, NameInputActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val calendarbtn: Button = view.findViewById(R.id.calendarbtn)
        calendarbtn.setOnClickListener {
            var next: Intent = Intent(requireActivity(), CalendarActivity::class.java).apply {
                putExtra("totalTime", formatElapsedTime(totalTime))
            }
            startActivity(next)
        }

        val listView: ListView = view.findViewById(R.id.listView)
        adapter = CustomAdapter(requireContext())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity, TimerActivity::class.java).apply {
                putExtra("position", position)
                putExtra("elapsedTime", timerData[position])
            }
            startActivityForResult(intent, 2)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            adapter.addItem(name!!)
            timerData.add(0L)
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            val elapsedTime = data?.getLongExtra("elapsedTime", 0) ?: 0
            val position = data?.getIntExtra("position", 0)!!

            totalTime += elapsedTime
            updateResultTime(totalTime)

            val formattedElapsedTime = formatElapsedTime(elapsedTime)

            adapter.updateTime(position, formattedElapsedTime)
            timerData[position] = elapsedTime
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultTimeTextView = view.findViewById(R.id.ResultTime)
        updateResultTime(totalTime)
    }

    private fun updateResultTime(totalTime: Long) {
        val formattedTotalTime = formatElapsedTime(totalTime)
        resultTimeTextView.text = formattedTotalTime
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val hours = (elapsedTime / 3600000)
        val minutes = (elapsedTime % 3600000) / 60000
        val seconds = (elapsedTime % 60000) / 1000
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}
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

    fun addItem(name: String) {
        this.add(Pair(name, "00:00:00"))
    }

    fun updateTime(position: Int, time: String) {
        val item = this.getItem(position)
        item?.let {
            this.remove(item)
            this.insert(Pair(item.first, time), position)
        }
    }
}
