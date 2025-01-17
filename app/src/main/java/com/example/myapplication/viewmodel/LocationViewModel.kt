package com.example.myapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.LocationData

class LocationViewModel:ViewModel(){
    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    fun updateLocation(newLocation : LocationData){
        _location.value = newLocation
    }


}