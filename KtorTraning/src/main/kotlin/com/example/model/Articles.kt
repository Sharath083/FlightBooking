package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


data class Article(val id: Int, val name: String, val password: String)
@Serializable
data class ArticleReq(val name:String,val password: String)

object Articles : Table("table") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 12)
    val password = varchar("password", 10)

    override val primaryKey = PrimaryKey(id)
}