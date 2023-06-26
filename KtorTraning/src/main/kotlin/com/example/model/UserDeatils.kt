package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserDetails(val id: Int, val name: String, val password: String)
@Serializable
data class UserReq(val name:String, val password: String)

object Details : Table("table") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 12)
    val password = varchar("password", 10)

    override val primaryKey = PrimaryKey(id)
}