package com.example.myapplication.Entities

data class Aircraft(
    var ID: Long = 0,
    var Name: String = "",
    var MakeModel: String = "",
    var FirstSeats: Int = 0,
    var BusinessSeats: Int = 0,
    var EconomySeats: Int = 0
) {
    override fun toString(): String {
        return Name
    }
}
