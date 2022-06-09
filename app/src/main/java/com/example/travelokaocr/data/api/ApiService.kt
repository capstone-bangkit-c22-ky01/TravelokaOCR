package com.example.travelokaocr.data.api

import com.example.travelokaocr.data.model.profile.AccessProfileResponse
import com.example.travelokaocr.data.model.HistoryResponse
import com.example.travelokaocr.data.model.ocr.KTPResultResponse
import com.example.travelokaocr.data.model.flight.FlightSearchResponse
import com.example.travelokaocr.data.model.auth.LoginResponse
import com.example.travelokaocr.data.model.auth.LogoutResponse
import com.example.travelokaocr.data.model.flight.BookingResponse
import com.greentea.travelokaocr_gt.data.model.auth.RegisResponse
import com.example.travelokaocr.data.model.auth.UpdateTokenResponse
import com.example.travelokaocr.data.model.ocr.ScanIDCardResponse
import com.example.travelokaocr.data.model.ocr.UpdateBookingStatus
import com.example.travelokaocr.data.model.ocr.UpdatedKTPResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/* Endpoint */
//AUTH
const val REGIS_ENDPOINT = "users"
const val LOGIN_ENDPOINT = "authentications"
const val GOOGLE_LOGIN_ENDPOINT = "auth/google"
const val TOKEN_HEADER = "Authorization"

//FLIGHT
const val FLIGHT_ENDPOINT = "flights"
const val FLIGHT_BOOKING = "flights/booking"
const val FLIGHT_BOOKING_UPDATE = "flights/booking/{id}"
const val DEPARTURE_QUERY = "departure"
const val DESTINATION_QUERY = "destination"
const val ID_QUERY = "id"

//OCR
const val KTP_SCANIDCARD_ENDPOINT = "/ktp"
const val KTP_RESULT_ENDPOINT = "ktpresult"

//EDIT PROFIL
const val NAME = "name"
const val EMAIL = "email"
const val PROFILE_PICTURE = "foto_profil"

interface ApiService {

    //REGISTER USER
    @POST(REGIS_ENDPOINT)
    suspend fun registerUser(
        @Body data: HashMap<String, String>
    ): Response<RegisResponse>

    //LOGIN USER
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(
        @Body data: HashMap<String, String>
    ): Response<LoginResponse>

    //UPDATE TOKEN
    @PUT(LOGIN_ENDPOINT)
    suspend fun updateToken(
        @Body data: HashMap<String, String?>
    ): Response<UpdateTokenResponse>

    //LOGOUT USER
    @HTTP(method = "DELETE", path = LOGIN_ENDPOINT, hasBody = true)
    suspend fun logoutUser(
        @Body data: HashMap<String, String?>
    ): Response<LogoutResponse>

    //LOGIN WITH GOOGLE
    @GET(GOOGLE_LOGIN_ENDPOINT)
    suspend fun googleLogin(): Response<LoginResponse>

    //GET PROFILE
    @GET(REGIS_ENDPOINT)
    suspend fun getProfile(
        @Header (TOKEN_HEADER) accessToken: String
    ): Response<AccessProfileResponse>

    //UPDATE PROFILE
    //still confused, because should we use raw json on here too?
    @FormUrlEncoded
    @PUT (REGIS_ENDPOINT)
    suspend fun updateProfile(
        @Field(NAME) name: String?,
        @Field(EMAIL) email: String?,
        @Field(PROFILE_PICTURE) foto_profil: Url?
    ): Response<AccessProfileResponse>

    //GET FLIGHT SEARCH
    //still under the development
    @GET(FLIGHT_ENDPOINT)
    suspend fun getFlightSearch(
        @Header(TOKEN_HEADER) accessToken: String,
    ): Response<FlightSearchResponse>

    //GET FLIGHT SEARCH BASED ON QUERY
    @GET(FLIGHT_ENDPOINT)
    suspend fun getFlightSearchWithQuery(
        @Header(TOKEN_HEADER) accessToken: String,
        @Query(DEPARTURE_QUERY) departure: String,
        @Query(DESTINATION_QUERY) destination: String,
    ): Response<FlightSearchResponse>

    //POST BOOKING
    @POST(FLIGHT_BOOKING)
    suspend fun flightBooking(
        @Header(TOKEN_HEADER) accessToken: String,
        @Body data: HashMap<String, Int>
    ): Response<BookingResponse>

    // POST Scan ID Card
    @Multipart
    @POST(KTP_SCANIDCARD_ENDPOINT)
    suspend fun scanIDCard(
        @Header(TOKEN_HEADER) accessToken: String,
        @Part file: MultipartBody.Part,
        @Part("data") data: RequestBody
    ) : Response<ScanIDCardResponse>

    //GET LIST HISTORY
    @GET(FLIGHT_BOOKING)
    suspend fun getHistory(
        @Header(TOKEN_HEADER) accessToken: String
    ): Response<HistoryResponse>

    //UPDATE BOOKING STATUS
    @PUT(FLIGHT_BOOKING_UPDATE)
    suspend fun updateBookingStatus(
        @Path("id") id: String,
        @Header(TOKEN_HEADER) accessToken: String
    ): Response<UpdateBookingStatus>

    @GET(FLIGHT_BOOKING)
    suspend fun updateBooking(
        @Header(TOKEN_HEADER) accessToken: String,
        @Query(ID_QUERY) id: String
    ): Response<BookingResponse>

    //SCAN ID CARD
    //still under development

    //RE-SCAN ID CARD
    //still under development

    //GET OCR RESULT
    @GET(KTP_RESULT_ENDPOINT)
    suspend fun getOCRResult(
        @Header(TOKEN_HEADER) accessToken: String
    ): Response<KTPResultResponse>

    //UPDATE OCR RESULT
    @PUT(KTP_RESULT_ENDPOINT)
    suspend fun updateRetrievedDataToDatabase(
        @Header(TOKEN_HEADER) accessToken: String,
        @Body data: HashMap<String, String>
    ): Response<UpdatedKTPResponse>

}