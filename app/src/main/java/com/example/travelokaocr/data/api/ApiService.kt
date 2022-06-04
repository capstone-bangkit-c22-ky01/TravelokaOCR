package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.model.AccessProfileResponse
import com.example.travelokaocr.data.model.HistoryResponse
import com.example.travelokaocr.data.model.KTPResultResponse
import com.example.travelokaocr.data.model.UpdateTokenResponse
import com.example.travelokaocr.data.model.flight.FlightSearchResponse
import com.greentea.travelokaocr_gt.data.model.LoginResponse
import com.greentea.travelokaocr_gt.data.model.RegisResponse
import com.example.travelokaocr.data.model.auth.LogoutResponse
import retrofit2.Response
import retrofit2.http.*

/* Endpoint */
//AUTH
const val REGIS_ENDPOINT = "users"
const val LOGIN_ENDPOINT = "/authentications"
const val GOOGLE_LOGIN_ENDPOINT = "auth/google"
const val ACCESS_PROFILE = "users"
const val TOKEN_HEADER = "Authorization"
const val REFRESH_TOKEN = "refreshToken"

//OCR
const val KTP_RESULT_ENDPOINT = "ktpresult"

//HISTORY
const val HISTORY_ENDPOINT = "flights/booking"

//FLIGHT
const val FLIGHT_ENDPOINT = "flights"
const val DEPARTURE_QUERY = "departure"
const val DESTINATION_QUERY = "destination"

//EDIT PROFIL
const val NAME = "name"
const val EMAIL = "email"
const val PROFILE_PICTURE = "foto_profil"

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
        @Field(REFRESH_TOKEN) refreshToken: HashMap<String, String?>
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

    //GET PROFILE
    @GET(ACCESS_PROFILE)
    suspend fun getProfile(
        @Body data: HashMap<String, String>
    ): Response<AccessProfileResponse>

    //UPDATE PROFILE
    @FormUrlEncoded
    @PUT (ACCESS_PROFILE)
    suspend fun updateProfile(
        @Field(NAME) name: String?,
        @Field(EMAIL) email: String?,
        @Field(PROFILE_PICTURE) foto_profil: Url?
    ): Response<AccessProfileResponse>

    //GET FLIGHT SEARCH
    @GET(FLIGHT_ENDPOINT)
    suspend fun getFlightSearch(
        @Header(TOKEN_HEADER) accessToken: String,
        @Query(DEPARTURE_QUERY) departure: String,
        @Query(DESTINATION_QUERY) destination: String,
    ): Response<FlightSearchResponse>

    //LOGOUT USER
    @HTTP(method = "DELETE", path = LOGIN_ENDPOINT, hasBody = true)
    suspend fun logoutUser(
        @Body data: HashMap<String, String?>
    ): Response<LogoutResponse>
}