package com.example.runescapemarketwatchv3.ApiLayer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://secure.runescape.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RuneScapeApi by lazy {
        retrofit.create(RuneScapeApi::class.java)
    }
}
