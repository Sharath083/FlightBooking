package com.example.logic

import com.example.data.request.BookDetails
import com.example.data.request.Flight
import com.example.data.request.Passenger
import com.example.data.request.Filter
import com.example.data.response.PassengerId
import com.example.data.response.BookDetailsOut
import com.example.data.response.PassengerLogin

interface DAOInterface {
    suspend fun addNewFlight(d: Flight): Flight?
    suspend fun removeFLight(id:String): Boolean
    suspend fun bookFlight(details: BookDetailsOut): BookDetails?
    suspend fun getFlight(fId:String):List<Flight>
    suspend fun userRegistration(details: Passenger):Passenger?
    suspend fun getPassengerId(name:String): PassengerId?
    suspend fun getFlightId(id:Int):List<BookDetailsOut?>
    suspend fun getFlightAll():List<Flight>
    suspend fun getPassengerCountByFlight(flightId:String):Long
    suspend fun getFlightById(passengerId:Int):List<Flight>
    suspend fun filterBySourceDestination(details:Filter):List<Flight>
    suspend fun userLogin(login: PassengerLogin):List<PassengerLogin>
    suspend fun removeUser(id: Int):Boolean





//    suspend fun journeyDuration(name:String):List<Flight>
}