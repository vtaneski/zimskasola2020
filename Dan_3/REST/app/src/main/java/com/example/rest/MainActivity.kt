package com.example.rest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherInfo()
    }

    private fun getWeatherInfo() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "http://164.8.250.18:8080/weather/rest/weather/forecast/MARIBOR"

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                val strResp = response.toString()
                val jsonArray = JSONArray(strResp)
                var str_locations = ""
                for (i in 0 until jsonArray.length()) {
                    val jsonInner: JSONObject = jsonArray.getJSONObject(i)
                    str_locations = str_locations + "\n" + jsonInner.get("locationName")
                }
                textView_locations.text = str_locations
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                textView_locations.text = "That didn't work!"
            })
        queue.add(stringReq)
    }
}

