package com.example.travelokaocr.data.models

import com.google.gson.annotations.SerializedName

data class KTPResultResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: DataKTPResult? = null
)

data class DataKTPResult(
    @field:SerializedName("nik")
    val nik: Long? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("sex")
    val sex: String? = null,

    @field:SerializedName("married")
    val married: String? = null,

    @field:SerializedName("nationality")
    val nationality: String? = null,

    @field:SerializedName("title")
    val title: String? = null,
)
