package com.example.travelokaocr.data.repository

import com.example.travelokaocr.data.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

class AuthRepository {

    suspend fun register(
        name: String,
        email: String,
        password: String,
        profile_picture: File? = null
    ) =
        RetrofitInstance.API_OBJECT.registerUser(name, email, password, profile_picture)

    suspend fun loginUser(email: String, password: String) =
        RetrofitInstance.API_OBJECT.loginUser(email, password)

    suspend fun googleLogin() =
        RetrofitInstance.API_OBJECT.googleLogin()
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }