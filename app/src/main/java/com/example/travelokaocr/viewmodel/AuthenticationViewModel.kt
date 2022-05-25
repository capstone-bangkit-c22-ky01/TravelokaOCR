package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelokaocr.data.RegisterResponse
import com.example.travelokaocr.data.UserDataRegister
import com.example.travelokaocr.data.models.LoginGoogleResponse
import com.example.travelokaocr.data.models.LoginResponse
import com.example.travelokaocr.data.repository.AuthenticationRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {
    //LOGIN
    val loginUsers: MutableLiveData<Response<LoginResponse>> = MutableLiveData()

    fun getLoginUsersResponse(email: String, password: String) =
        viewModelScope.launch {
            val response = authenticationRepository.loginUser(email, password)
            loginUsers.value = response
        }

    //LOGIN WITH GOOGLE
    val loginGoogleUsers: MutableLiveData<Response<LoginGoogleResponse>> = MutableLiveData()

    fun getLoginGoogleUsersResponse(email: String, password: String) =
        viewModelScope.launch {
            val response = authenticationRepository.loginUser(email, password)
            loginUsers.value = response
        }

    //REGISTER
    val registerUsers: MutableLiveData<Response<RegisterResponse>?> = MutableLiveData()

    fun getRegisterUsersResponse(name: String, email: String, password: String, profile_picture: File? = null) =
        viewModelScope.launch {
            val response = authenticationRepository.register(name, email, password, profile_picture)
            registerUsers.value = response
        }
}