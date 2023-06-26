package com.example.dao

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object DatabaseFactory{
    fun init() {
        Database.connect(
            "jdbc:mysql://localhost:3306/ktordb", driver = "com.mysql.cj.jdbc.Driver",
            user = "root", password = "root"
        )
    }
    suspend fun <T> dbQuery(block: () ->T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}

