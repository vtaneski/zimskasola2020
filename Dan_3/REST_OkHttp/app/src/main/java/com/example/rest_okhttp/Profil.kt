package com.example.rest_okhttp

import java.io.Serializable

data class Profil (
    var ime :String? ,
    var priimek :String?,
    var email :String?,
    var telesnaVisina :Double?
) : Serializable