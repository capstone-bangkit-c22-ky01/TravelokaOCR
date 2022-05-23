package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.AuthenticationRepository
import java.io.File

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    fun registerUser(name: String, email: String, password: String, profile_picture: File? = null) = authenticationRepository.register(name, email, password, profile_picture)
}