package com.example.travelokaocr.data.models

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<UserDataHistoryNew>? = null
)

data class UserDataHistory( //adjusted to the OLD History API
    @field:SerializedName("idResult")
    val idResult: String? = null,

    @field:SerializedName("time_departure")
    val time_departure: String? = null,

    @field:SerializedName("time_arrived")
    val time_arrived: String? = null,

    @field:SerializedName("price")
    val price: String? = null
)

data class UserDataHistoryNew( //adjusted to the NEW History API
    @field:SerializedName("purchaseMonth")
    val purchaseMonth: String? = null,

    @field:SerializedName("bookingID")
    val bookingID: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("cityDepart")
    val cityDepart: String? = null,

    @field:SerializedName("cityArrive")
    val cityArrive: String? = null,

    @field:SerializedName("purchaseStatus")
    val purchaseStatus: Boolean? = null
)