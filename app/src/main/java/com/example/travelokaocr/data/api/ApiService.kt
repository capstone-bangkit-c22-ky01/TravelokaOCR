package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.UserData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

const val REGISTER_ENDPOINT = "users"
const val NAME_FIELD = "name"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"

interface ApiService {

    @FormUrlEncoded
    @POST(REGISTER_ENDPOINT)
    suspend fun registerUser(
        @Field(NAME_FIELD) username: String,
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String,
    ): Response<UserData>

}