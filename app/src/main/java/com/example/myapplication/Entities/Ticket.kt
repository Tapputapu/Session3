package com.example.myapplication.Entities

import java.time.LocalDate
import java.time.LocalDateTime

data class Ticket(
    var ID: Long = 0,
    var TicketDetailID: Long = 0,
    var BookingReference: String = "",
    var Firstname: String = "",
    var Lastname: String = "",
    var Phone: String = "",
    var BirthDate: String = "",
    var Email: String = "",
    var PassportNumber: String = "",
    var PassportCountryID: Long = 0,
    var PurchaseDate: String = "",
    var SeatNumber: Int? = null,
    var StatusID: Long = 0

) {
    fun getBirthDate(): LocalDate {
        return LocalDateTime.parse(BirthDate).toLocalDate()
    }
}
