package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.date.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {

    DatabaseFactory.init(environment.config)
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

    val config=environment.config
    val subject = config.property("jwt.subject").getString()
    val audience = config.property("jwt.audience").getString()
    val issuer = config.property("jwt.issuer").getString()
    val configRealm = config.property("jwt.realm").getString()
    val secretKey = config.property("jwt.secret").getString()


//    install(Authentication){
//        jwt("token"){
//            realm= configRealm
//            verifier(
//                JWT.require(Algorithm.HMAC256(secretKey))
//                    .withAudience(audience)
//                    .withIssuer(issuer)
//                    .withSubject(subject)
//                    .build()
//            )
//            validate { jwtCredential ->
//                if (jwtCredential.payload.audience.contains(audience)
//                    && jwtCredential.payload.expiresAt.time> getTimeMillis()
//                ){
//                    JWTPrincipal(jwtCredential.payload)
//                }else{
//                    null
//                }
//            }
//            challenge { _, _ ->
//                call.respond(HttpStatusCode.Unauthorized, "The token is invalid.")
//            }
//
//        }
//    }


//    configureSecurity(environment.config)


}



