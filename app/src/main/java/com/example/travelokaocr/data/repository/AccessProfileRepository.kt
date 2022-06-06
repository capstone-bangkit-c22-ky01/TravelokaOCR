package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.profile.AccessProfileResponse
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson
import retrofit2.http.Url

class AccessProfileRepository {
    //ACCESS PROFILE
    fun profileUser(data: String): LiveData<Resources<AccessProfileResponse?>> = liveData{
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

    //EDIT PROFILE
    //still under development
    fun updateUser(name: String, email: String, foto_profil: Url): LiveData<Resources<AccessProfileResponse?>> = liveData{
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<AccessProfileResponse?>>()
        val response = RetrofitInstance.API_OBJECT.updateProfile(name, email, foto_profil)
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