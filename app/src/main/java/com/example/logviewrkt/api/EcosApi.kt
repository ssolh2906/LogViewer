package com.example.logviewrkt.api

import retrofit2.Call
import retrofit2.http.GET

interface EcosApi {

    @GET("/")
    fun fetchContents() : Call<String>
}