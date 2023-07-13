package com.example.dao

import com.example.objects.*

interface DAOInterface {
    suspend fun addNewFlight(d: Flight): Flight?
    suspend fun removeFLight(id:String): Boolean
    suspend fun bookFlight(details: BookDetailsOut): BookDetails?
    suspend fun getFlight(fId:String):List<Flight>
    suspend fun addNewPassenger(d: Passenger):Passenger?
    suspend fun getPassengerId(name:String): PassengerId?
    suspend fun getFlightId(id:Int):List<BookDetailsOut?>
    suspend fun getFlightAll():List<Flight>
    suspend fun getPassengerCountByFlight(flightId:String):Long

//    suspend fun journeyDuration(name:String):List<Flight>
}