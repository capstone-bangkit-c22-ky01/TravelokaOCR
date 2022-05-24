package com.example.travelokaocr.data

import com.google.gson.annotations.SerializedName
import java.io.File

data class RegisterResponse(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("foto_profil")
    val profile_picture: File? = null

)

data class UserDataRegister(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user_id")
    val user_id: String

)
