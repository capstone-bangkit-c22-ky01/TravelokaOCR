package com.example.travelokaocr.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class AccessProfileResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: AccessProfileData? = null
)

data class AccessProfileData(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("foto_profil")
    val foto_profil: Url? = null
)