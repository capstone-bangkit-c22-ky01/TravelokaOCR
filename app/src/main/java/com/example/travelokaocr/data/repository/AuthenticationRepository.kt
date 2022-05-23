package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.UserData
import com.example.travelokaocr.data.api.ApiService
import java.io.File

class AuthenticationRepository(private val apiService: ApiService) {

    fun register(name: String, email: String, password: String, profile_picture: File? = null): LiveData<Result<UserData?>> = liveData{

    }
}