package com.example.objects

import org.jetbrains.exposed.sql.Table

object BookDetailsDao :Table("booking_table"){
    val ticketNo=integer("ticketno").autoIncrement("123451234")
    val passengerId=integer("passengerid")
    val flightNumber=varchar("flightnumber",45)
    override val primaryKey = PrimaryKey(ticketNo)

}