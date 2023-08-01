package com.smhrd.stucamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class NameInputActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)

        editTextName = findViewById(R.id.editTextName)
        buttonAdd = findViewById(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("name", name)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
