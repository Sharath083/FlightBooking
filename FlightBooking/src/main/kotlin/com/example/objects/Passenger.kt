package com.example.objects

import kotlinx.serialization.Serializable

@Serializable
data class Passenger(val id:Int,val name:String)

data class PassengerId(val id:Int)



