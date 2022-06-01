package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.AccessProfileResponse
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson

class AccessProfileRepository {
    fun profileUser(data: HashMap<String, String>): LiveData<Resources<AccessProfileResponse?>> = liveData{
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<AccessProfileResponse?>>()
        val response = RetrofitInstance.API_OBJECT.getProfile(data)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), AccessProfileResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }
}