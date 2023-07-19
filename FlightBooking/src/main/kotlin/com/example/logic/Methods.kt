package com.example.logic

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import io.ktor.server.config.*

class Methods(private val config: ApplicationConfig) {

    fun timeTaken(departureTime: String, arrivalTime: String): Int{
        val startTime = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

        val endTime = LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

        return ((endTime - startTime) / 3600)
    }
    fun timeConvert(time: String): Int {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

    }
    fun tokenGenerator(name:String):String{

        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()
        val audience = config.property("jwt.audience").getString()
        val myRealm = config.property("jwt.realm").getString()
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("user",name)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(secret))
    }
}