package com.example.plugins

import com.example.logic.DAOImplementation
import com.example.routes.flightFunctions
import com.example.routes.passengerFunctions
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    val daoImplementation=DAOImplementation()

    routing {
        flightFunctions(daoImplementation)
        passengerFunctions(daoImplementation)

    }
}
