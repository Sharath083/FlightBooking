package com.example.dao

import java.time.LocalTime
import java.time.format.DateTimeFormatter
class Methods {
    fun timeTaken(departureTime: String, arrivalTime: String): Int{
        val startTime = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

        val endTime = LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

        return ((endTime - startTime) / 3600)
    }
    fun timeConvert(time: String): Int {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()

    }
}