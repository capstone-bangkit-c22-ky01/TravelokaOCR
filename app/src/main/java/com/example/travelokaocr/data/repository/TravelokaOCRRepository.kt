package com.example.travelokaocr.data.repository

import com.example.travelokaocr.data.api.RetrofitInstance

class TravelokaOCRRepository {
    suspend fun getOCRResult(accessToken: String) =
        RetrofitInstance.API_OBJECT.getOCRResult(accessToken)

}