package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.AccessProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccessProfileViewModel(private val repo: AccessProfileRepository): ViewModel() {
    fun profileUser(data: String) = repo.profileUser(data)

    fun updateUser(accessToken: String, dataUsername: RequestBody?, dataEmail: RequestBody?, imageMultipart: MultipartBody.Part?) = repo.updateUser(accessToken, dataUsername, dataEmail, imageMultipart)
}