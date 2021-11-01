package com.example.myapplication.Entities

import java.time.LocalDate
import java.time.LocalDateTime

data class TicketDetail(
    var ID: Long = 0,
    var CabinTypeID: Long = 0,
    var ScheduleID: Long = 0,
    var Rate: Double = 0.0
)