package com.example.rest_okhttp

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray

class OKHttpRestClient {

    private val endpoint = "http://164.8.250.18:8080/weather/rest"
    private val key = "3cbd4dbd-3217-4389-94a0-b9ae0627ed56"

    /*
    {
        "bodyHeigth": 1.85,
        "bodyWeigth": 95.0,
        "favouritePlaces": [

        ],
        "name": "Luka",
        "surname": "Pavlič",
        "userId": "dummy.user2@email.com"
     }
     */
    fun addUser(u :Profil) {
        val json = "{\"bodyHeigth\":\"${u.telesnaVisina}\",\"name\":\"${u.ime}\",\"surname\":\"${u.priimek}\",\"userId\":\"${u.email}\"}"
        Log.i(BuildConfig.APPLICATION_ID,"OKHttpRestClient.addUser: $json")

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)
        // val body = RequestBody.create(json, mediaType) -> Deprecated

        val client = OkHttpClient()
        val request = Request.Builder().
            url("$endpoint/users").
            header("API-Key", key).
            put(body).
            build()

        val response = client.newCall(request).execute()
        Log.i(BuildConfig.APPLICATION_ID,"Response Code : " + response.code)
        Log.i(BuildConfig.APPLICATION_ID,"Response: " + response.body?.string())
    }

    fun getAllUsers() : ArrayList<Profil> {
        Log.i(BuildConfig.APPLICATION_ID,"OKHttpRestClient.getAllUsers")
        Log.i(BuildConfig.APPLICATION_ID,"GET $endpoint/users")
        val client = OkHttpClient()
        val request = Request.Builder().
            url("$endpoint/users").
            build()

        val response = client.newCall(request).execute()
        val responseBody=response.body?.string()
        Log.i(BuildConfig.APPLICATION_ID,"Response Code : " + response.code)
        Log.i(BuildConfig.APPLICATION_ID, "Response: $responseBody")

        val ja=JSONArray(responseBody)
        val res= arrayListOf<Profil>()
        for (i in 0 until ja.length()) {
            val jo=ja.getJSONObject(i)
            val p=Profil(
                jo.getString("name"),
                jo.getString("surname"),
                jo.getString("userId"),
                jo.getDouble("bodyHeigth"))
            res.add(p)
        }
        return res
    }
}