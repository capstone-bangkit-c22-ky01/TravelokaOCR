package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.RegisterResponse
import com.example.travelokaocr.data.model.*
import retrofit2.Response
import retrofit2.http.*
import java.io.File

//AUTH
const val REGISTER_ENDPOINT = "users"
const val LOGIN_ENDPOINT = "authentications"
const val GOOGLE_LOGIN_ENDPOINT = "auth/google"
const val NAME_FIELD = "name"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"
const val PROFILE_PICTURE = "profile_picture"
const val TOKEN_HEADER = "Authorization"
const val REFRESH_TOKEN = "refreshToken"

//OCR
const val KTP_RESULT_ENDPOINT = "ktpresult"

//HISTORY
const val HISTORY_ENDPOINT = "" //coming soon, still waiting for CC

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

    //LOGIN WITH GOOGLE
    @GET(GOOGLE_LOGIN_ENDPOINT)
    suspend fun googleLogin(): Response<LoginResponse>

    //UPDATE TOKEN
    @FormUrlEncoded
    @PUT(LOGIN_ENDPOINT)
    suspend fun updateToken(
        @Field(REFRESH_TOKEN) refreshToken: String
    ): Response<UpdateTokenResponse>

    //GET OCR RESULT
    @GET(KTP_RESULT_ENDPOINT)
    suspend fun getOCRResult(
        @Header(TOKEN_HEADER) accessToken: String
    ): Response<KTPResultResponse>

    //GET LIST HISTORY
    @GET(HISTORY_ENDPOINT)
    suspend fun getHistory(
        @Header(TOKEN_HEADER) accessToken: String
    ): Response<HistoryResponse>
}