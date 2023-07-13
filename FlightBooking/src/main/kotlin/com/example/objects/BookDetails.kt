package com.example.objects

import kotlinx.serialization.Serializable

@Serializable
data class BookDetails(val passengerId:Int,val flightNumber:String)

data class BookDetailsOut(val ticket:Int, val passengerId:Int, val flightNumber:String)


