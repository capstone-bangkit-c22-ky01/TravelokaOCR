package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.AuthRepository

class AuthViewModel(private val repo: AuthRepository): ViewModel() {
    //REGISTER
    fun regisUser(data: HashMap<String, String>) = repo.regisUser(data)

    //LOGIN
    fun loginUser(data: HashMap<String, String>) = repo.loginUser(data)
}