package com.example.rest_retrofit

import java.io.Serializable

data class User (
    val bodyHeigth: Double?,
    val bodyWeigth: Double?,
    val favouritePlaces: ArrayList<String>?,
    val name: String?,
    val surname: String?,
    val userId: String
) : Serializable