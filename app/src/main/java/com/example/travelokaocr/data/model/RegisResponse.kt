package com.greentea.travelokaocr_gt.data.model

import com.google.gson.annotations.SerializedName

data class RegisResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: UserDataRegister? = null
)

data class UserDataRegister(
    @field:SerializedName("user_id")
    val user_id: String? = null,
)
