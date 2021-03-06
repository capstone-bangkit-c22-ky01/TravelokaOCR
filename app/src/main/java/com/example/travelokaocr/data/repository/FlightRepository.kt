package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.flight.*
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson

class FlightRepository {

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
    fun flightBook(accessToken: String, flightID: HashMap<String, Int>):
            LiveData<Resources<BookingResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<BookingResponse?>>()
        val response = RetrofitInstance.API_OBJECT.flightBooking(accessToken, flightID)
        if (response.isSuccessful){
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), BookingResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

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

    //DETAIL HISTORY
    fun detailHistory(dataBookingID: String, accessToken: String):
            LiveData<Resources<DetailHistoryResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<DetailHistoryResponse?>>()
        val response = RetrofitInstance.API_OBJECT.getDetailHistory(dataBookingID, accessToken)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), DetailHistoryResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //DELETE BOOKING BY ID
    fun deleteBookingById(dataBookingID: String, accessToken: String):
            LiveData<Resources<DeleteBookingResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<DeleteBookingResponse?>>()
        val response = RetrofitInstance.API_OBJECT.deleteBookingById(dataBookingID, accessToken)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), DeleteBookingResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //DELETE BOOKING
    fun deleteAllBooking(accessToken: String):
            LiveData<Resources<DeleteBookingResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<DeleteBookingResponse?>>()
        val response = RetrofitInstance.API_OBJECT.deleteAllBooking(accessToken)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), DeleteBookingResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }
}

