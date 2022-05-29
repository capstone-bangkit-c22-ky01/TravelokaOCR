package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.viewmodel.AuthenticationViewModel

@Suppress("UNCHECKED_CAST")
class AuthenticationViewModelFactory (private val authRepository: AuthRepository)
    : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthenticationViewModel(authRepository) as T
        }
    }