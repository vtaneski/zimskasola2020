package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button2.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            db.collection("test/").add(
                mapOf(
                    "ime" to "Viktor",
                    "priimek" to "Taneski"
                )
            )
        }
    }
}
