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
                "/1/{endIndex}" +   //결과시작인덱스, 끝인덱스
                "/064Y001" +         // for DD
                "/DD" +
                "/{startYYMMDD}"+   // 시작점
                "/{endYYMMDD}" +    // 끝점
                "/0001000/?/?/"
    )
    fun fetchIndex(
        @Path("startYYMMDD") startYYMMDD : String,
        @Path("endYYMMDD") endYYMMDD : String,
        @Path("endIndex") endIndex:String
    ): Call<EcosResponse>
}