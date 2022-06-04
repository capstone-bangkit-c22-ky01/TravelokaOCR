package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.HistoryResponse
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson
import com.example.travelokaocr.data.model.flight.FlightSearchResponse

class FlightRepository {
    //FLIGHT SEARCH
    //still under development

    //FLIGHT SEARCH WITH QUERY
    fun flightSearch(accessToken: String, departure: String, destination: String):
            LiveData<Resources<FlightSearchResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<FlightSearchResponse?>>()
        val response = RetrofitInstance.API_OBJECT.getFlightSearchWithQuery(accessToken, departure, destination)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), FlightSearchResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //POST BOOKING
    //still under the development

    //HISTORY
    fun history(accessToken: String):
            LiveData<Resources<HistoryResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<HistoryResponse?>>()
        val response = RetrofitInstance.API_OBJECT.getHistory(accessToken)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), HistoryResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //UPDATE BOOKING
    //still under development
}

