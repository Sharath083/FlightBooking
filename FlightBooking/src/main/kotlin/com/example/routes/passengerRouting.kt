package com.example.routes

import com.example.dao.PassengerDao
import com.example.logic.Methods
import com.example.data.response.TravelDetails
import com.example.data.response.TravelTime
import com.example.data.request.Passenger
import com.example.data.request.Filter
import com.example.data.response.BookDetailsOut
import com.example.logic.DAOImplementation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere


fun Route.passengerFunctions(daoImplementation: DAOImplementation) {
    route("/passenger") {
        val methods = Methods()

        post("/register") {
            val name = call.receive<Passenger>()
            daoImplementation.userRegistration(name)
            call.respond("${name.name} is Added")
        }
        get("/travelTime") {
            val input = call.receive<Passenger>()
            val id = daoImplementation.getPassengerId(input.name)
            val bookingDetails = id?.let { it1 ->
                daoImplementation.getFlightId(it1.id)
            }

            runBlocking {
                val result = bookingDetails?.map { number ->
                    async {
                        number?.let { it1 ->
                            daoImplementation.getFlight(number.flightNumber).map { time -> time.let {
                                    val duration = methods.timeTaken(time.departureTime, time.arrivalTime)
                                    TravelTime(it1.ticket, "$duration Hours", it1.flightNumber)
                                }

                            }
                        }
//                            ?.sortedByDescending { it.duration.substring(0,1).toInt() }

                    }
                }
//                result?.awaitAll()?.map { t->t?.sortedByDescending { it.duration.substring(0,1).toInt() }
//                }


                if (result != null) {
                    call.respond(result.awaitAll())
                } else {
                    call.respond("NO DATA")
                }
            }
        }
        get("/allFlights"){
            val f=daoImplementation.getFlightAll().sortedBy { methods.timeTaken(it.departureTime,it.arrivalTime) }
//            minBy {methods.timeTaken(it.departureTime,it.arrivalTime) }

            call.respond(f)
        }

        post("/book") {
            val params = call.receive<BookDetailsOut>()

            if (daoImplementation.getFlight(params.flightNumber).isNotEmpty()) {
                daoImplementation.bookFlight(params)
                call.respond("${params.flightNumber} has booked")
            } else {
                call.respond("Flight with ${params.flightNumber} Does Not Exists")
            }
        }

        get("/flights") {
            val input = call.receive<Passenger>()
            val id = daoImplementation.getPassengerId(input.name)
            if (id != null) {
                val bookingDetails = daoImplementation.getFlightId(id.id)
                runBlocking {
                    val result = bookingDetails.map { number ->
                        async {
                            number?.let { it1 ->
                                TravelDetails(it1.ticket, daoImplementation.getFlight(number.flightNumber))
                            }

                        }
                    }
                    call.respond(result.awaitAll())
                }
            } else {
                call.respond("${input.name} does not exists")
            }
        }
        get("/count/{flightId}"){
            val input=call.parameters["flightId"]?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val count=daoImplementation.getPassengerCountByFlight(input)
            call.respond("$count Passengers has booked the Flight")

        }
        get("/Id={id}"){
            val id=call.parameters["id"]?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val result=daoImplementation.getFlightById(id.toInt())
            if(result.isNotEmpty()){
                call.respond(result)
            }
            else{
                call.respond("$id does not have any bookings")
            }
        }
        get("/filterSource&Destination"){
            val input=call.receive<Filter>()
            val result=daoImplementation.filterBySourceDestination(input).sortedBy { methods.timeTaken(it.departureTime,it.arrivalTime) }

            if(result.isNotEmpty()){

                call.respond(result )
            }
            else{
                call.respond("THERE ARE NO FLIGHTS BETWEEN ${input.source} AND ${input.destination}")
            }
        }


        get("/filterBy/{type}") {
            val input=call.parameters["type"]?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val result = daoImplementation.getFlightAll()

            when (input.lowercase()) {
                "price" -> call.respond(result.sortedBy { it.price })
                "duration" -> call.respond(result.sortedBy {
                    methods.timeTaken(it.departureTime, it.arrivalTime) })
                else-> call.respond("ENTER VALID FILTER")
            }

        }

        delete("/delete/{id}") {
            val id=call.parameters["id"]?:return@delete call.respondText("Missing Id", status = HttpStatusCode.BadRequest)
            val res= daoImplementation.removeUser(id.toInt())
            if(res){
                call.respond("$id is removed")
            }
            else{
                call.respond("User does not exists")
            }

        }

    }
}


