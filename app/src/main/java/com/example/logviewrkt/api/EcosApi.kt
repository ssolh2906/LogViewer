package com.example.logviewrkt.api

import com.example.logviewrkt.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EcosApi { // 추상함수 가져서 인터페이스인가봐....

    //@GET("/")
    //fun fetchContents() : Call<String>

    @GET(
        "StatisticSearch/" +
                BuildConfig.ECOS_API_KEY +
                "/json" +
                "/kr" +
                "/1/10" +  //결과시작인덱스, 끝인덱스
                "/028Y015" +
                "/MM" +
                "/{startYYMM}"+ // 시작점
                "/{endYYMM}" + // 끝점
                "/1080000/?/?/"
    )
    fun fetchIndex(@Path("startYYMM") startYYMM : String, @Path("endYYMM") endYYMM : String): Call<EcosResponse>
}