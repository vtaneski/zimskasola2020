package com.example.rest_retrofit

interface ServiceCallback {
    fun onSuccess(users: ArrayList<User>)

    fun onError(throwable: Throwable)
}


