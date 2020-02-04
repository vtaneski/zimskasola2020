package com.example.preferences

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = getPreferencesData()

        button.setOnClickListener {
            val property = editText.text.toString()
            savePreferenceData(property)
        }

        buttonClear.setOnClickListener {
            clearPreferencesData()
        }
    }

    private fun savePreferenceData(property: String) {
        val pref: SharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE)
        val editor: Editor = pref.edit()

        editor.putString("property", property)
        editor.apply()
        Toast.makeText(applicationContext, "Property saved", Toast.LENGTH_SHORT).show()
    }

    private fun getPreferencesData(): String {
        val pref: SharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE)

        return pref.getString("property", "property does not exist") ?: "property does not exist"
    }

    private fun clearPreferencesData() {
        val pref: SharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE)
        val editor: Editor = pref.edit()
        editor.clear()
        editor.commit()
    }
}
