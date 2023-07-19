package com.example.routes


import com.example.data.request.AdminLogin
import com.example.logic.DAOImplementation
import com.example.data.request.Flight
import com.example.exceptions.FlightNotFoundException
import com.example.exceptions.SameFlightIdException
import com.example.logic.Methods
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.flightFunctions(daoImplementation: DAOImplementation){
    route("/flight"){
        val config = environment?.config
        val methods=Methods(config!!)

        post("/adminLogin"){
            val adminLogin=call.receive<AdminLogin>()
            if(adminLogin.name == "Admin" && adminLogin.password=="123456") {
                val token=methods.tokenGenerator(adminLogin.name)
                call.respond(hashMapOf("token" to token ,"Expires in " to "6  Minutes"))
            }
            else{
                call.respond("Imposter")
            }

        }
        authenticate("Admin") {
            post("/add") {

                val params = call.receive<Flight>()


                if (daoImplementation.getFlight(params.flightNumber).isEmpty()) {

                    (daoImplementation.addNewFlight(params) != null)
                    call.respond("ADDED NEW FLIGHT")
                }
                else{
                    throw SameFlightIdException("${params.flightNumber} Already Exists. Give Different Flight Id")

                }

            }
        }
        authenticate("Admin") {
            delete("/remove/{id?}") {
                val id = call.parameters["id"] ?: return@delete call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                if (daoImplementation.removeFLight(id)) {
                    call.respond("$id is removed ")
                } else {
//                call.respond("$id Does not Exists")
                    throw FlightNotFoundException("$id Does Not Exists")
                }

            }
        }
        authenticate("Admin") {
            get("/count/{flightId}"){
                val input=call.parameters["flightId"]?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )

                val count=daoImplementation.getPassengerCountByFlight(input)
                call.respond("$count Passengers has booked the Flight")

            }
        }
    }

}