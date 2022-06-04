package com.example.travelokaocr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelokaocr.data.repository.AccessProfileRepository
import retrofit2.http.Url

class AccessProfileViewModel(private val repo: AccessProfileRepository): ViewModel() {
    fun profileUser(data: HashMap<String, String>) = repo.profileUser(data)

    fun updateUser(name: String, email: String, foto_profil: Url) = repo.updateUser(name, email, foto_profil)
}