package com.example

import com.example.dao.DatabaseFactory
import com.example.logic.Methods
import io.ktor.server.application.*

import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
//    install(Authentication) {
//        form("auth-form") {
//            userParamName = "username"
//            passwordParamName = "password"
//            validate { credentials ->
//                if (credentials.name == "Admin" && credentials.password == "123456") {
//                    UserIdPrincipal(credentials.name)
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
//            }
//        }
//    }
    Methods(environment.config)
    DatabaseFactory.init(environment.config)
    configureStatusPage()
    configureSecurity()
    configureSerialization()
    configureRouting()
}
