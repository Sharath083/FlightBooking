package com.example.objects

import kotlinx.serialization.Serializable



@Serializable
data class Flight(
    val flightNumber:String,
    val source:String,
    val destination:String,  val departureTime: String, val arrivalTime: String
)


