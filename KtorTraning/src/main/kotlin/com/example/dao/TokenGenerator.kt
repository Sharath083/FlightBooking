package com.example.dao

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.TokenResponse
import com.example.model.UserReq
import io.ktor.server.config.*
import io.ktor.util.date.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.*



class TokenGenerator(private val config:ApplicationConfig) {

    suspend fun createToken(input: UserReq): TokenResponse =
        coroutineScope {
            var accessToken = ""
            val createdAt = getTimeMillis()
            val expireInMs = config.property("jwt.expireInMs").getString().toLong() ?: 1_200_000L
            try {
                async {
                    val subject = config.property("jwt.subject").getString()
                    val audience = config.property("jwt.audience").getString()
                    val domain = config.property("jwt.issuer").getString()
                    val secretKey = config.property("jwt.secret").getString()

                    JWT.create()
                        .withAudience(audience)
                        .withIssuer(domain)
                        .withExpiresAt(Date(createdAt + expireInMs))
                        .withSubject(subject)
                        .withClaim("name", input.name)
                        .withClaim("password", input.password)
                        .sign(Algorithm.HMAC256(secretKey))
                }.also {
                    accessToken = it.await()
                }


            } catch (ex: Exception) {
                println("Something failed when creating access token for $${ex.message}")
//                throw TokenCreationException()
            }

            TokenResponse(accessToken, createdAt,expireInMs)



        }


}