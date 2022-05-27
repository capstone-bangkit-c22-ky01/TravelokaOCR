package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.TravelokaOCRRepository
import com.example.travelokaocr.viewmodel.TravelokaOCRViewModel

@Suppress("UNCHECKED_CAST")
class TravelokaOCRViewModelFactory (private val travelokaOCRRepository: TravelokaOCRRepository)
    : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TravelokaOCRViewModel(travelokaOCRRepository) as T
        }
    }