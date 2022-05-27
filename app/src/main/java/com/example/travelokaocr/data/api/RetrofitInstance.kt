package com.example.travelokaocr.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private const val Base_URL = ""

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            val level = HttpLoggingInterceptor.Level.BODY
            logging.setLevel(level)

            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val API_OBJECT: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}