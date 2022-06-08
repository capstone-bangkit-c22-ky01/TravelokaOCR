package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.OCRRepository
import com.example.travelokaocr.viewmodel.OCRScreenViewModel

@Suppress("UNCHECKED_CAST")
class OCRScreenViewModelFactory (
    private val repo: OCRRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OCRScreenViewModel(repo) as T
    }
}