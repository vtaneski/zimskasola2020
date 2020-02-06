package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button.setOnClickListener {
            val fbs = FirebaseAuth.getInstance()

            fbs.signInWithEmailAndPassword(
                "viktor@email.com", "geslo123456"
            ).addOnSuccessListener {
                Toast.makeText(this, "ODLIČNO ${fbs.currentUser}", Toast.LENGTH_LONG).show()

                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener{error ->
                Log.d("Login", error.toString())
            }
        }
    }
}