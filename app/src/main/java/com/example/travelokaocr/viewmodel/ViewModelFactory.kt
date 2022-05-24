package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.models.LoginResponse
import com.example.travelokaocr.data.repository.AuthenticationRepository
import retrofit2.Response

class ViewModelFactory private constructor(
    private val authenticationRepository: AuthenticationRepository
): ViewModelProvider.NewInstanceFactory() {

}