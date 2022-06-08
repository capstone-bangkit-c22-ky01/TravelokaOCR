package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.FlightRepository

class FlightViewModel(private val repo: FlightRepository) : ViewModel() {

    //FLIGHT SEARCH
    fun flightSearch(accessToken: String, departure: String, destination: String) =
        repo.flightSearch(accessToken, departure, destination)

    // Post Booking
    fun flightBook(accessToken: String, flightID: HashMap<String, Int>) =
        repo.flightBook(accessToken, flightID)

    //HISTORY
    fun history(accessToken: String) = repo.history(accessToken)
}