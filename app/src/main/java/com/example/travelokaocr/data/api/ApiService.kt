package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.RegisterResponse
import com.example.travelokaocr.data.UserDataRegister
import com.example.travelokaocr.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.File

const val REGISTER_ENDPOINT = "users"
const val LOGIN_ENDPOINT = "authentications"
const val LOGIN_GOOGLE_ENDPOINT = "auth/google"
const val NAME_FIELD = "name"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"
const val PROFILE_PICTURE = "profile_picture"

interface ApiService {

    //REGISTER USER
    @FormUrlEncoded
    @POST(REGISTER_ENDPOINT)
    suspend fun registerUser(
        @Field(NAME_FIELD) name: String,
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String,
        @Field(PROFILE_PICTURE) profile_picture: File? = null
    ): Response<RegisterResponse>

    //LOGIN USER
    @FormUrlEncoded
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<LoginResponse>

    //LOGIN GOOGLE
    @FormUrlEncoded
    @POST(LOGIN_GOOGLE_ENDPOINT)
    suspend fun loginGoogleUser(
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<LoginGoogleResponse>
}