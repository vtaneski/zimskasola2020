package com.example.rest_retrofit

import android.util.Log
import androidx.annotation.Nullable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val user_email = "dummy.user2@email.com"
const val base_url = "http://164.8.250.18:8080/weather/rest/"
const val api_key = "API-Key: 3cbd4dbd-3217-4389-94a0-b9ae0627ed56"

interface RetrofitService {

    @GET("users")
    fun getAllUsers(): Call<List<User>>

    @PUT("users")
    fun putUser(@Body user: User): Call<Void>
}

class RetrofitRestClient {

    fun updateUser(u :User) {
        Log.i("RetrofitService","RetrofitRestClient.putUser")
        val res = getService().putUser(u).execute()
        Log.i("RetrofitService",res.toString())
    }

    fun getUsers() : ArrayList<User> {
        Log.i("RetrofitService","RetrofitRestClient.getAllUsers")
        val res = getService().getAllUsers().execute()
        Log.i("RetrofitService",res.body().toString())
        return convert(res.body())
    }

    private fun getService() :RetrofitService {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RetrofitService::class.java)
    }

    private fun convert(r :List<User>?):ArrayList<User> {
        val res= arrayListOf<User>()
        r?.forEach {
            res.add(User(it.bodyHeigth, null, null,
                it.name,it.surname,it.userId))
        }
        return res
    }

    //ASINHRONO
    /*fun getUsersAsynch(callback: ServiceCallback){
        Log.i("RetrofitService","RetrofitRestClient.getUsersAsynch")
        var users: ArrayList<User>? = null

        getService().getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.code() == 200) {
                    Log.i("RetrofitService","Rresponse: ${response.body()}")
                    users = convert(response.body())

                    callback.onSuccess(users ?: arrayListOf<User>())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })
    }*/
}
