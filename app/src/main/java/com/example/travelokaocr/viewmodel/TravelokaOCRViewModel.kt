package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelokaocr.data.RegisterResponse
import com.example.travelokaocr.data.models.HistoryResponse
import com.example.travelokaocr.data.models.KTPResultResponse
import com.example.travelokaocr.data.models.LoginGoogleResponse
import com.example.travelokaocr.data.models.LoginResponse
import com.example.travelokaocr.data.repository.AuthenticationRepository
import com.example.travelokaocr.data.repository.TravelokaOCRRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class TravelokaOCRViewModel(private val travelokaOCRRepository: TravelokaOCRRepository): ViewModel() {
    //LOGIN
    val ktpResult: MutableLiveData<Response<KTPResultResponse>> = MutableLiveData()
    val history: MutableLiveData<Response<HistoryResponse>> = MutableLiveData()

    fun getKTPResultResponse(accessToken: String) =
        viewModelScope.launch {
            val response = travelokaOCRRepository.getOCRResult(accessToken)
            ktpResult.value = response
        }

    fun getHistory(accessToken: String) =
        viewModelScope.launch {
            val response = travelokaOCRRepository.getHistory(accessToken)
        }
}