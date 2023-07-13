package com.example.objects

import kotlinx.serialization.Serializable

@Serializable
data class TravelDetails(val ticketNumber:Int,val flight:List<Flight>?)


@Serializable
data class TravelTime(val ticketNumber:Int,val duration:String,val flightId:String)