package com.example.routes

import com.example.dao.DAOFacadeImpl
import com.example.model.UserDetails
import com.example.model.UserReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.insertDetails(){
    val ob= DAOFacadeImpl()
    route("/user"){
        post {
            val user=call.receive<UserReq>()
            ob.addNewArticle(user)
            call.respond("Details are inserted")

        }
        get {
            call.respond(ob.allArticles())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val result=ob.Article(id.toInt())
            call.respondNullable(result)
        }
        put {
            val user=call.receive<UserDetails>()
            ob.editArticle(user)

            call.respond(" Details are updated")
        }
    }
}
