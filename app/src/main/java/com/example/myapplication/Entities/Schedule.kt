package com.example.myapplication.Entities

import java.time.LocalDate
import java.time.LocalDateTime

data class Schedule(
    var ID: Long = 0,
    var Date: String = "",
    var Time: String = "",
    var AircraftID: Long = 0,
    var RouteID: Long = 0,
    var FlightNumber: String = "",
    var EconomyPrice: Int = 0

) {
    fun getDate(): LocalDate {
        return LocalDateTime.parse(Date).toLocalDate()
    }

}
