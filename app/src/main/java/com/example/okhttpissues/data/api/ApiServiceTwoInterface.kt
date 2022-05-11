package com.example.okhttpissues.data.api

import com.example.okhttpissues.data.api_model.CommentDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceTwoInterface {
    @GET("comments")
    fun getCommentsData(): Call<ArrayList<CommentDataItem>>
}