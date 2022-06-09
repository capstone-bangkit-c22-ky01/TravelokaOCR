package com.example.travelokaocr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.OCRRepository
import java.util.HashMap


class OCRResultViewModel(private val repo: OCRRepository) : ViewModel() {

    val setLoadingOCRResultDialog = MutableLiveData(false)

    fun updateRetrievedDataToDatabase(accessToken: String, dataToBeSendToAPI: HashMap<String, String>) =
        repo.updateRetrievedDataToDatabase(accessToken, dataToBeSendToAPI)

    fun updateBookingStatus(accessToken: String, dataBookingID: String) =
        repo.updateBookingStatus(accessToken, dataBookingID)

}