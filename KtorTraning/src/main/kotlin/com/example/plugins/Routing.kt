package com.example.plugins

import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
       customerRouting()
        listOrders()
        getOrderRoute()
        totalizeOrderRoute()
        insertDetails()
    }

}