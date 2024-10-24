package com.example.myapplication.helpers

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.myapplication.models.Rides
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun getRides(): List<Rides> {
    var ridesList = remember { mutableStateOf<List<Rides>>(emptyList()) }
    val dbref = FirebaseDatabase.getInstance().getReference("rides")

    LaunchedEffect(Unit) {
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fetchedRides = mutableListOf<Rides>()
                for (snapshot in dataSnapshot.children) {

                    // Deserialize each snapshot into a Rides object
                    val ride = snapshot.getValue(Rides::class.java)
                    ride?.let {
                        fetchedRides.add(it)
                    }
                }
                ridesList.value = fetchedRides
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "DB error, unable to post location")
            }

        })
    }
    return ridesList.value
}