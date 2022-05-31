package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.model.HistoryResponse
import com.example.travelokaocr.data.model.KTPResultResponse
import com.example.travelokaocr.data.model.UpdateTokenResponse
import com.greentea.travelokaocr_gt.data.model.LoginResponse
import com.greentea.travelokaocr_gt.data.model.RegisResponse
import retrofit2.Response
import retrofit2.http.*

/* Endpoint */
//AUTH
const val REGIS_ENDPOINT = "users"
const val LOGIN_ENDPOINT = "/authentications"
const val GOOGLE_LOGIN_ENDPOINT = "auth/google"
const val PROFILE_PICTURE = "foto_profil"
const val TOKEN_HEADER = "Authorization"
const val REFRESH_TOKEN = "refreshToken"

//OCR
const val KTP_RESULT_ENDPOINT = "ktpresult"

//HISTORY
const val HISTORY_ENDPOINT = "" //coming soon, still waiting for CC

interface ApiService {

    //REGISTER USER
    @POST(REGIS_ENDPOINT)
    suspend fun registerUser(
        @Body data: HashMap<String, String>
//        @Part foto_profil: MultipartBody.Part? coming soon
    ): Response<RegisResponse>

    //LOGIN USER
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(
        @Body data: HashMap<String, String>
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