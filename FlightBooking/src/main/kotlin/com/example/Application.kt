package com.example

import com.example.dao.DatabaseFactory
import io.ktor.server.application.*

import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    DatabaseFactory.init(environment.config)

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    configureSecurity()
    configureSerialization()
    configureRouting()
}
