package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelokaocr.data.models.LoginResponse
import com.example.travelokaocr.data.repository.AuthenticationRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {
    //LOGIN
    val loginUsers: MutableLiveData<Response<LoginResponse>> = MutableLiveData()

    fun getLoginUsersResponse(email: String, password: String) =
        viewModelScope.launch {
            val response = authenticationRepository.loginUser(email, password)
            loginUsers.value = response
        }
}