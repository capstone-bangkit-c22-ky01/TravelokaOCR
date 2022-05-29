package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.AuthRepository

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository
): ViewModelProvider.NewInstanceFactory() {

}