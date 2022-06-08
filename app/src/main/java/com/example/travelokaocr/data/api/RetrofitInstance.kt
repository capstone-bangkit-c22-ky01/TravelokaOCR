package com.example.travelokaocr.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val logging = HttpLoggingInterceptor()
            val level = HttpLoggingInterceptor.Level.BODY
            logging.setLevel(level)

            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("https://ocr-app-eoyzxrvqla-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        val API_OBJECT: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}