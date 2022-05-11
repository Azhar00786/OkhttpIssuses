package com.example.okhttpissues.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConnectorTwo(private val commentUrl: String) {
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(commentUrl)
            .build()
    }
}