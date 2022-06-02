package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.FlightRepository
import com.example.travelokaocr.viewmodel.FlightViewModel

@Suppress("UNCHECKED_CAST")
class FlightViewModelFactory (
    private val repo: FlightRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FlightViewModel(repo) as T
        }
    }