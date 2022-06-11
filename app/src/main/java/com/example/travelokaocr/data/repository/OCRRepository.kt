package com.example.travelokaocr.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.travelokaocr.data.api.RetrofitInstance
import com.example.travelokaocr.data.model.ocr.KTPResultResponse
import com.example.travelokaocr.data.model.ocr.ScanIDCardResponse
import com.example.travelokaocr.data.model.ocr.UpdateBookingStatus
import com.example.travelokaocr.data.model.ocr.UpdatedKTPResponse
import com.example.travelokaocr.utils.Resources
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

class OCRRepository {

    // Post Scan ID Card
    fun scanIDCard(accessToken: String, file: MultipartBody.Part, data: RequestBody):
        LiveData<Resources<ScanIDCardResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<ScanIDCardResponse?>>()
        val response = RetrofitInstance.API_OBJECT.scanIDCard(accessToken, file, data)
        if (response.isSuccessful){
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        }else{
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), ScanIDCardResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    // Retrieve ID Card Result
    fun retrieveIDCardResult(accessToken: String):
        LiveData<Resources<KTPResultResponse?>> = liveData {
        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<KTPResultResponse?>>()
        val response = RetrofitInstance.API_OBJECT.getOCRResult(accessToken)
        if (response.isSuccessful){
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        }else{
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), KTPResultResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }
    }

    fun updateRetrievedDataToDatabase(accessToken: String, dataToBeSendToAPI: HashMap<String, String>):
        LiveData<Resources<UpdatedKTPResponse?>> = liveData {

        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<UpdatedKTPResponse?>>()
        val response = RetrofitInstance.API_OBJECT.updateRetrievedDataToDatabase(accessToken, dataToBeSendToAPI)
        if (response.isSuccessful){
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        }else{
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), UpdatedKTPResponse::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }

    }

    fun updateBookingStatus(accessToken: String, dataBookingID: String, dataToBeSendToAPI1: HashMap<String, String>):
        LiveData<Resources<UpdateBookingStatus?>> = liveData {

        emit(Resources.Loading)
        val returnValue = MutableLiveData<Resources<UpdateBookingStatus?>>()
        val response = RetrofitInstance.API_OBJECT.updateBookingStatus(dataBookingID, accessToken, dataToBeSendToAPI1)
        if (response.isSuccessful){
            returnValue.value = Resources.Success(response.body())
            emitSource(returnValue)
        }else{
            val error = Gson().fromJson(response.errorBody()?.stringSuspending(), UpdateBookingStatus::class.java)
            response.errorBody()?.close()
            returnValue.value = Resources.Success(error)
            emitSource(returnValue)
        }

    }


}