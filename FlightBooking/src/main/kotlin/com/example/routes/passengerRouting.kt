package com.example.routes

import com.example.logic.Methods
import com.example.data.response.TravelDetails
import com.example.data.response.TravelTime
import com.example.data.request.Passenger
import com.example.data.request.Filter
import com.example.data.response.PassengerLogin
import com.example.exceptions.FilterDoesNotExistException
import com.example.exceptions.FlightNotFoundException
import com.example.exceptions.InvalidLoginDetails
import com.example.exceptions.UserNotFoundException
import com.example.logic.DAOImplementation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

import kotlin.time.Duration.Companion.milliseconds


fun Route.passengerFunctions(daoImplementation: DAOImplementation) {
    route("/passenger") {
        val config = environment?.config

        val methods = Methods(config!!)

        post("/register") {
            val details = call.receive<Passenger>()
            daoImplementation.userRegistration(details)
            call.respond("${details.name} is Added")
        }
        post("/login") {
            val details=call.receive<PassengerLogin>()
            val result=daoImplementation.userLogin(details)
            if(result.isNotEmpty()){
                val token=methods.tokenGenerator(details.name,"login user")

                call.respond(hashMapOf("token" to token ,"Expires in " to "6  Minutes"))
            }
            else{
                throw InvalidLoginDetails()
            }


        }
        authenticate {

            get("/travelTime") {
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("user").asString()
                val id = daoImplementation.getPassengerId(name)
                val bookingDetails = id?.let { it1 ->
                    daoImplementation.getFlightId(it1.id)
                }

                runBlocking {
                    val result = bookingDetails?.map { number ->
                        async {
                            number?.let { it1 ->
                                daoImplementation.getFlight(number.flightNumber).map { time ->
                                    time.let {
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


                    if (!(result.isNullOrEmpty())) {
                        call.respond(result.awaitAll())
                    } else {
                        call.respond("You Has Zero Booked Flights")
                    }
                }
            }
        }
        get("/allFlights"){
            val flightList=daoImplementation.getFlightAll().sortedBy { methods.timeTaken(it.departureTime,it.arrivalTime) }
//            minBy {methods.timeTaken(it.departureTime,it.arrivalTime) }

            call.respond(flightList)
        }

        authenticate {
            post("/book/{flightId}") {
                val flightId = call.parameters["flightId"]?: return@post call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("user").asString()
                val user = daoImplementation.getPassengerId(name)
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())


                if (daoImplementation.getFlight(flightId).isNotEmpty()) {

                    if (user != null) {
                        daoImplementation.bookFlight(flightId,user.id)
                        call.respond("$flightId has booked AND SESSION WILL EXPIRES IN ${(expiresAt)?.milliseconds}")
                    }
                    else{
                        throw UserNotFoundException("$name is not register")
                    }
                }
                else {
//                    call.respond("Flight with $flightId Does Not Exists")
                    throw FlightNotFoundException("Flight with $flightId Does Not Exists")
                }
            }
        }
        authenticate {
            get("/all-flights-Of-Passenger") {
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("user").asString()
                val id = daoImplementation.getPassengerId(name)
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
//                    call.respond("$name does not exists")
                    throw UserNotFoundException("$name does not exists")
                }
            }
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
                else-> throw FilterDoesNotExistException()
            }

        }
        authenticate {

            delete("/deleteAccount") {
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("user").asString()
                val details = daoImplementation.getPassengerId(name)
                if(details!=null) {
                    daoImplementation.removeUser(details.id)
                    call.respond("$name Account Is Deleted")

                }
                else {
                    throw UserNotFoundException("$name Does Not Have Account")
                }

            }
        }
        authenticate {
            delete("cancelFlight/{id}") {
                val flightNumber=call.parameters["id"]?: return@delete call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("user").asString()
                val details = daoImplementation.getPassengerId(name)

                if(details!=null) {
                    if (daoImplementation.cancelTicket(details.id,flightNumber)){
                        call.respond("Ticket Has Been Cancelled")
                    }
                    else{
                        throw FlightNotFoundException("$name Has not Booked The Flight")
                    }
                }
                else {
                    throw UserNotFoundException("$name Does Not Have Account")
                }
            }
        }

    }
}


