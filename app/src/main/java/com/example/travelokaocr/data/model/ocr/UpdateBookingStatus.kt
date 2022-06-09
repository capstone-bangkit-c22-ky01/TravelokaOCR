package com.example.travelokaocr.data.model.ocr

import com.google.gson.annotations.SerializedName

data class UpdateBookingStatus(

	@field:SerializedName("data")
	val data: DataUpdateBookingStatus,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataUpdateBookingStatus(

	@field:SerializedName("bookingId")
	val bookingId: String
)
