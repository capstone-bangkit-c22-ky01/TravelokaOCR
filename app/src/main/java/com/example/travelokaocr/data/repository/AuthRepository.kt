package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.auth.LoginResponse
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson
import com.example.travelokaocr.data.model.auth.LogoutResponse
import com.greentea.travelokaocr_gt.data.model.auth.RegisResponse
import com.example.travelokaocr.data.model.auth.UpdateTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class AuthRepository {
    //REGIS
    fun regisUser(data: HashMap<String, String>): LiveData<Resources<RegisResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<RegisResponse?>>()
        val response = RetrofitInstance.API_OBJECT.registerUser(data)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), RegisResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //LOGIN
    fun loginUser(data: HashMap<String, String>): LiveData<Resources<LoginResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<LoginResponse?>>()
        val response = RetrofitInstance.API_OBJECT.loginUser(data)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), LoginResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //UPDATE TOKEN
    fun updateToken(data: HashMap<String, String?>): LiveData<Resources<UpdateTokenResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<UpdateTokenResponse?>>()
        val response = RetrofitInstance.API_OBJECT.updateToken(data)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), UpdateTokenResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    //LOGOUT
    fun logoutUser(data: HashMap<String, String?>): LiveData<Resources<LogoutResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<LogoutResponse?>>()
        val response = RetrofitInstance.API_OBJECT.logoutUser(data)
        if(response.isSuccessful) {
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        } else {
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), LogoutResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }