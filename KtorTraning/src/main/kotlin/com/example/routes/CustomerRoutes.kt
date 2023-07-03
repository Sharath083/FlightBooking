package com.example.routes

import com.example.model.Customer
import com.example.model.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Route.customerRouting(){

    route("/customer"){
        rateLimit {
            get {
                if (customerStorage.isNotEmpty()) {
                    call.respond(customerStorage)
                } else {
                    call.respondText("No customers found", status = HttpStatusCode.OK)
                }

            }
        }
        get("/download"){//we can get image through
            val file = File("C:\\Users\\sharathkumar.b\\Pictures\\Screenshots/image.png")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "ktorTraining.png")
                    .toString())
//            call.response.status(HttpStatusCode(22,"Pic downloaded"))


            call.respondFile(file)



        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
            call.respondText("Fetched Data")
        }
        post {
            val customer=call.receive<Customer>()
            if ((customerStorage.any { it.id==customer.id })){

                call.respondText("Customer with id ${customer.id} already exists",status = HttpStatusCode.BadRequest)
            }
            else{
                customerStorage.add(customer)
                call.respondText("Customer stored correctly",status = HttpStatusCode.Created)
            }
        }
        delete("{id?}") {
            val id =call.parameters["id"]?:return@delete call.respondText("Missing id", status = HttpStatusCode.BadRequest)
            val customer= customerStorage.removeIf { it.id==id }
            if(customer){
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            }
            else{
                call.respondText("Customer with $id is not found", status = HttpStatusCode.NotFound)
            }
        }



    }
}