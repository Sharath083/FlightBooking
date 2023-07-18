package com.example.data.request

import kotlinx.serialization.Serializable



@Serializable
data class Flight(
    val flightNumber:String,
    val airline:String,
    val price:Int,
    val source:String,
    val destination:String,  val departureTime: String, val arrivalTime: String
)


