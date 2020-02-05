package com.example.rest_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() , ServiceCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_users.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val users = CoroutineScope(Dispatchers.IO).async {
                    //asynchronous call to REST endpoint
                    getAllUsers()
                    //getAllUsersAsynch()
                }.await()

                //after the asynchronous call, we can use the result and show it in the Main thread
                var usersStr = ""
                for (user in users) {
                    usersStr = "$usersStr\n${user.name} ${user.surname}"
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

    override fun onSuccess(users: ArrayList<User>) {
        //after the asynchronous call, we can use the result and show it in the Main thread
        Log.i("Main info", "updating Main UI")
        var usersStr = ""
        for (user in users) {
            usersStr = "$usersStr\n${user.name} ${user.surname}"
        }

        textView_users.text = usersStr
    }

    override fun onError(throwable: Throwable) {
        TODO("not implemented")
    }

    private fun getAllUsers(): ArrayList<User> {
        val client = RetrofitRestClient()

        return client.getUsers()
    }

    private fun postUser() {
        val user = User(1.8,null, null, "Luka",
            "Pavlic1", "dummy.user2@email.com")

        val client = RetrofitRestClient()
        client.updateUser(user)
    }

    /*private fun getAllUsersAsynch() {
        val client = RetrofitRestClient()

        client.getUsersAsynch(this)
    }*/
}
