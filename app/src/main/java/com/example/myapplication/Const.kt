package com.example.myapplication

import android.util.Log
import com.example.myapplication.Entities.Airport
import com.example.myapplication.Entities.CabinType
import java.time.LocalDate

const val INSERT = "POST"
const val UPDATE = "PUT"
const val SELECT = "GET"
const val DELETE = "DELETE"
const val TIMEOUT = 3000
const val BASE_URL = "http://172.17.1.30:8003/api/"

val FAIL_MESSAGE: (String) -> String = {
    Log.d("DATABASE", "Fail $it Action.")
    ""
}

var outboundID = 0L
var returnID = 0L
/*
var startDateC: String? = null
var endDateC: String? = null
var depID = 0L
var arrID = 0L*/

//search information
var DepartureAirport: Airport? = null
var ArrivalAirport: Airport? = null
var IsReturn = false
var DepartureDate: LocalDate? = null
var ReturnDate: LocalDate? = null
var Adult: Int = 0
var Child: Int = 0
var Baby: Int = 0
var CabinType: CabinType? = null
var IsLast = true