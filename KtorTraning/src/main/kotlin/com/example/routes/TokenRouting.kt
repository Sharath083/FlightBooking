package com.example.routes

import com.example.dao.DAOFacadeImpl
import com.example.dao.TokenGenerator
import com.example.model.UserReq
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.token(tokenClass:TokenGenerator,daoFacadeImpl:DAOFacadeImpl){
    route("/token"){
        post("/login") {

            val customer=call.receive<UserReq>()
            if(daoFacadeImpl.userLogin(customer)!=null){
                val token=tokenClass.createToken(customer)
                call.respond(token)

            }
            else{
                call.respond("Invalid User Details")
            }

        }

        authenticate ("token"){
            get ("/validate"){
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("name").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")

            }
        }
    }
}