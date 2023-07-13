package com.example.objects

import org.jetbrains.exposed.sql.Table


object PassengerDao: Table("passenger_table"){
    val id = integer("id").autoIncrement()
    val name=varchar("name",45)
    override val primaryKey=PrimaryKey(id)


}