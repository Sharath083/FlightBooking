package com.example.routes

import com.example.dao.DAOImplementation
import com.example.objects.Flight
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

fun Route.flightFunctions(daoImplementation: DAOImplementation){
    route("/flight"){
        post("/add") {

            val params=call.receive<Flight>()

            daoImplementation.addNewFlight(params)
            call.respond("ADDED NEW FLIGHT")
        }
        delete("/remove/{id?}") {
            val id = call.parameters["id"]?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            if(daoImplementation.removeFLight(id)){
                call.respond("$id is removed ")
            }
            else{
                call.respond("$id Does not Exists")
            }

        }
    }

}