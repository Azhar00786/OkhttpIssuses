package com.example.okhttpissues.data.api

import com.example.okhttpissues.data.api_model.IssuesItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceInterface {
    @GET("issues")
    fun getOkhttpIssueData(): Call<ArrayList<IssuesItem>>
}