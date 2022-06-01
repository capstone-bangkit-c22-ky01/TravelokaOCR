package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.viewmodel.AccessProfileViewModel

class AccessProfileFactory(private val repo: AccessProfileRepository)
    : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AccessProfileViewModel(repo) as T
        }
    }