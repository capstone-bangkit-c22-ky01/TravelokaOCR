package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.AccessProfileRepository

class AccessProfileViewModel(private val repo: AccessProfileRepository): ViewModel() {
    fun profileUser(data: HashMap<String, String>) = repo.profileUser(data)
}