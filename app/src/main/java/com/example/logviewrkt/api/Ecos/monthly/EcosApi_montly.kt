package com.example.logviewrkt.api.Ecos.monthly

import com.example.logviewrkt.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EcosApi { // 추상함수 가져서 인터페이스인가봐....

    @GET(
        "StatisticSearch/" +
                BuildConfig.ECOS_API_KEY +
                "/json" +
                "/kr" +
                "/1/{endIndex}" +   //결과시작인덱스, 끝인덱스
                "/028Y015" +         // for MM
                "/MM" +
                "/{startYYMM}"+   // 시작점
                "/{endYYMM}" +    // 끝점
                "/{indexID}/?/?/"
    )
    fun fetchIndex(
        @Path("startYYMM") startYYMM : String,
        @Path("endYYMM") endYYMM : String,
        @Path("endIndex") endIndex:String,
        @Path("indexID") indexID :String
    ): Call<EcosResponse>
}