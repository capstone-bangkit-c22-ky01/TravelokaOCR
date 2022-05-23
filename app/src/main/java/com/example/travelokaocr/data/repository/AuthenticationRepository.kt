package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.ApiService
import com.example.travelokaocr.data.Result
import com.example.travelokaocr.data.UserDataRegister
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.lang.Exception

class AuthenticationRepository(private val apiService: ApiService) {

    fun register(name: String, email: String, password: String, profile_picture: File? = null): LiveData<Result<UserDataRegister?>?> = liveData{
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<UserDataRegister?>?>()
            val response = apiService.registerUser(name, email, password, profile_picture)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                val error = Gson().fromJson(response.errorBody()?.stringSuspending(), UserDataRegister::class.java)
                response.errorBody()?.close()
                returnValue.value = Result.Success(error)
                emitSource(returnValue)
            }
        }
        catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

    suspend fun loginUser(email: String, password: String) =
        apiService.loginUser(email, password)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }