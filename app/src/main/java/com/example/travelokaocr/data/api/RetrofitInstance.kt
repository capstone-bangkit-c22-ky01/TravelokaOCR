package com.example.travelokaocr.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            val level = HttpLoggingInterceptor.Level.BODY
            logging.setLevel(level)

            val client = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("https://ocr-app-eoyzxrvqla-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val API_OBJECT: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}