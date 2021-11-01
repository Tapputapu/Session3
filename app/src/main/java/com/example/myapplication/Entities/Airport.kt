package com.example.myapplication.Entities

data class Airport(
    var ID: Long = 0,
    var CountryID: Long = 0,
    var IATACode: String = "",
    var Name: String = ""

) {
    override fun toString(): String {
        return Name
    }
}
