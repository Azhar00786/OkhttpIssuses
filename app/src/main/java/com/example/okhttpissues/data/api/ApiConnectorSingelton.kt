package com.example.okhttpissues.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConnectorSingelton {
    private const val BASE_URL = "https://api.github.com/repos/square/okhttp/"

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}