package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelokaocr.data.RegisterResponse
import com.example.travelokaocr.data.model.LoginResponse
import com.example.travelokaocr.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class AuthViewModel(private val repo: AuthRepository): ViewModel() {
    //LOGIN
    val login: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    fun getLoginResponse(email: String, password: String) =
        viewModelScope.launch {
            val response = repo.loginUser(email, password)
            login.value = response
        }

    //LOGIN WITH GOOGLE
    fun getGoogleLoginResponse() =
        viewModelScope.launch {
            val response = repo.googleLogin()
            login.value = response
        }

    //REGISTER
    val registerUsers: MutableLiveData<Response<RegisterResponse>?> = MutableLiveData()
    fun getRegisterUsersResponse(name: String, email: String, password: String, profile_picture: File? = null) =
        viewModelScope.launch {
            val response = repo.register(name, email, password, profile_picture)
            registerUsers.value = response
        }
}