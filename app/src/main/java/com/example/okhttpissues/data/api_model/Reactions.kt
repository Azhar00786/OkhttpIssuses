package com.example.okhttpissues.data.api_model

import com.google.gson.annotations.SerializedName

data class Reactions(
   /* @SerializedName("plusOne")
    val +1: Int,
    @SerializedName("minusOne")
    val -1: Int,*/
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    val rocket: Int,
    val total_count: Int,
    val url: String
)