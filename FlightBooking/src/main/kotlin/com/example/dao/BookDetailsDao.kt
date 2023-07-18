package com.example.dao

import org.jetbrains.exposed.sql.Table

object BookDetailsDao :Table("booking_table"){
    val ticketNo=integer("ticketno").autoIncrement("123451234")
//    val passengerId=integer("passengerid")
    val passengerId=reference("passengerid",PassengerDao.id)

//    val flightNumber=varchar("flightnumber",45)
    val flightNumber=reference("flightnumber",FLightDetailsDao.flightNumber)


    override val primaryKey = PrimaryKey(ticketNo)

}