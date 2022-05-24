package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.AuthenticationRepository

class ViewModelFactory private constructor(
    private val authenticationRepository: AuthenticationRepository
): ViewModelProvider.NewInstanceFactory() {

}