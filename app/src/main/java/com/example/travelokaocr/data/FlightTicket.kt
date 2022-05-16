package com.example.travelokaocr.data

data class FlightTicket(
    val timeDepart: String,
    val cityDepartCode: String,
    val flightDuration: String,
    val flightType: String,
    val timeArrive: String,
    val cityArriveCode: String,
    val price: String,
    val airplaneImage: Int,
    val airplaneName: String,
)
