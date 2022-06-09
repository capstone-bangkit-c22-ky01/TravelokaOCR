package com.example.travelokaocr.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.data.repository.OCRRepository
import com.example.travelokaocr.viewmodel.OCRResultViewModel

@Suppress("UNCHECKED_CAST")
class OCRResultViewModelFactory (
    private val repo: OCRRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OCRResultViewModel(repo) as T
    }
}