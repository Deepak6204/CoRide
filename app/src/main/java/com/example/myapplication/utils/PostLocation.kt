package com.example.myapplication.utils

import com.example.myapplication.models.Rides
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object PostLocation {
    private val database = FirebaseDatabase.getInstance().reference

    fun postLocation(rides : Rides) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if(userId != null){
            database.child("rides").child(userId).setValue(rides)
        }
    }

}