package com.example.myapplication.Entities

data class Route(
    var ID: Long = 0,
    var DepartureAirportID: Long = 0,
    var ArrivalAirportID: Long = 0,
    var Distance: Int = 0,
    var FlightTime: Int = 0

) {
}
