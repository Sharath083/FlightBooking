package com.example.plugins

import com.example.dao.DAOFacadeImpl
import com.example.dao.TokenGenerator
import com.example.routes.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.configureRouting() {
    val config = environment.config

    val tokenProviderService = TokenGenerator(config)

    val daoFacadeImpl=DAOFacadeImpl()

    routing {
        customerRouting()
        listOrders()
        getOrderRoute()
        totalizeOrderRoute()
        insertDetails(tokenProviderService,daoFacadeImpl)
    }

}
