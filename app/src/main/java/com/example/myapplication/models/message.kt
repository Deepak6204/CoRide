package com.example.myapplication.models

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0
)
