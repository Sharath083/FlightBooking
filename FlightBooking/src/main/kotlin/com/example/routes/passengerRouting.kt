package com.example.routes

import com.example.dao.*
import com.example.dao.Methods
import com.example.objects.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll


fun Route.passengerFunctions(daoImplementation: DAOImplementation) {
    route("/passenger") {
        val methods = Methods()

        post("/newPassenger") {
            val name = call.receive<Passenger>()
            daoImplementation.addNewPassenger(name)
            call.respond("${name.name} is inserted")
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
        get("/{id}"){
            val i=call.parameters["id"]?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            call.respond(daoImplementation.dds(i))
        }
    }
}


