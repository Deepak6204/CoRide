package com.example.myapplication.models

data class Rides(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var destination: String? = "",
    var user:String? = "",
    var uid: String? = ""
)
