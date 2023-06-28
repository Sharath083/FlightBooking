package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.util.date.*

fun Application.configureSecurity(config: ApplicationConfig) {



    val subject = config.property("jwt.subject").getString()
    val audience = config.property("jwt.audience").getString()
    val issuer = config.property("jwt.issuer").getString()
    val configRealm = config.property("jwt.realm").getString()
    val secretKey = config.property("jwt.secret").getString()


    install(Authentication) {
        jwt("token") {
            realm = configRealm
            verifier(
                JWT.require(Algorithm.HMAC256(secretKey))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .build()
            )
        }
    }
        install(Authentication){
            jwt("token"){
                validate { jwtCredential ->
                    if (jwtCredential.payload.audience.contains(audience)
                        && jwtCredential.payload.expiresAt.time> getTimeMillis()
                    ){
                        JWTPrincipal(jwtCredential.payload)
                    }else{
                        null
                    }
                }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "The token is invalid.")
            }

        }
    }
}
