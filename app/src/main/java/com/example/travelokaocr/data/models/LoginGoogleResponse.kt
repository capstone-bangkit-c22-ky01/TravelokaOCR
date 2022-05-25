package com.example.travelokaocr.data.models

import com.google.gson.annotations.SerializedName

data class LoginGoogleResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: UserDataLoginGoogle? = null
)

data class UserDataLoginGoogle(

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("expiresIn")
    val expiresIn: Int? = null,

    @field:SerializedName("profile")
    val profile: ProfileLoginGoogle? = null

)

data class ProfileLoginGoogle(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("picture")
    val picture: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("email_verified")
    val email_verified: Boolean = true

)
