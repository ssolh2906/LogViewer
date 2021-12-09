package com.example.logviewrkt.api

import com.example.logviewrkt.BuildConfig
import retrofit2.Call
import retrofit2.http.GET

interface EcosApi {

    //@GET("/")
    //fun fetchContents() : Call<String>

    @GET(
        "StatisticSearch/" +
                BuildConfig.ECOS_API_KEY +
                "/json" +
                "/kr" +
                "/10/20" +  //결과시작인덱스, 끝인덱스
                "/028Y015" +
                "/MM" +
                "/201101" + // 시작점
                "/202101" + // 끝점
                "/1080000/?/?/"
    )
    fun fetchIndex(): Call<EcosResponse>
}