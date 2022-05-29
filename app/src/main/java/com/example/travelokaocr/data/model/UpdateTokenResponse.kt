package com.example.travelokaocr.data.model

import com.google.gson.annotations.SerializedName

data class UpdateTokenResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: UserDataToken? = null
)

data class UserDataToken(
    @field:SerializedName("accessToken")
    val accessToken: String? = null,
)
