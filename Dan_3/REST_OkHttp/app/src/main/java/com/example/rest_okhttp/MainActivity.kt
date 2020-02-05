package com.example.rest_okhttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_users.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val users = CoroutineScope(Dispatchers.IO).async {
                    //asynchronous call to REST endpoint
                    getAllUsers()
                }.await()

                //after the asynchronous call, we can use the result and show it in the Main thread
                var usersStr = ""
                for (user in users) {
                    usersStr = "$usersStr\n${user.ime} ${user.priimek}"
                }

                textView_users.text = usersStr
            }
        }

        button_postUser.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.IO).async {
                    //asynchronous call to REST endpoint
                    postUser()
                }.await()
            }
        }
    }

    private fun getAllUsers(): ArrayList<Profil> {
        val client = OKHttpRestClient()

        val users = client.getAllUsers()
        return users
    }

    private fun postUser() {
        val user = Profil("Luka", "Pavlic", "dummy.user2@email.com", 1.8)

        val client = OKHttpRestClient()
        client.addUser(user)
    }
}
