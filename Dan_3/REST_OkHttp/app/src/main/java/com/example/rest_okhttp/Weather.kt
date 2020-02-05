package com.example.rest_okhttp

import java.io.Serializable
import java.util.*

data class Weather (
    var actual: Boolean,
    var clouds: Int,
    var forecast: Boolean,
    var humidity: Int,
    var lat: Double,
    var locationName: String,
    var lon: Int,
    var pressure: Int,
    var sunrise: Double,
    var sunset: Double,
    var tempFrom: Double,
    var tempTo: Double,
    var timeStamp: Long
): Serializable