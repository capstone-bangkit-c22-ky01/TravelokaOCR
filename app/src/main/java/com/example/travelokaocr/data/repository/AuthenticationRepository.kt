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

    suspend fun register(name: String, email: String, password: String, profile_picture: File? = null) =
        apiService.registerUser(name, email, password, profile_picture)


    suspend fun loginUser(email: String, password: String) =
        apiService.loginUser(email, password)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }