package com.example.routes

import com.example.dao.DAOFacadeImpl
import com.example.model.Article
import com.example.model.ArticleReq
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.insertDetails(){
    val ob= DAOFacadeImpl()
    route("/user"){
        post {
            val user=call.receive<ArticleReq>()
            ob.addNewArticle(user)
            call.respond("Details are inserted")

        }
    }
}
