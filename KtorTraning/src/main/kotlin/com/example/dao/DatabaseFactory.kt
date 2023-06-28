package com.example.dao

import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object DatabaseFactory{
    fun init(config: ApplicationConfig) {
        val url=config.property("db.url").getString()
        val driver=config.property("db.driver").getString()
        val user=config.property("db.user").getString()
        val password=config.property("db.password").getString()
        Database.connect(url, driver , user , password)
    }

    suspend fun <T> dbQuery(block: () ->T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}

