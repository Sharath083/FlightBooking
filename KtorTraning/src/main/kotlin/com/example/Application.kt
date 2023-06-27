package com.example

import com.example.dao.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {

    DatabaseFactory.init()
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
//    install(XHttpMethodOverride)
    install(RateLimit){
        register {
            rateLimiter(limit = 1, refillPeriod = 60.seconds)
        }
    }
    install(StatusPages) {
        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }

    }

    configureRouting()
    configureSecurity(environment.config)

}


