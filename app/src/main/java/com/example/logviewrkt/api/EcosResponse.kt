package com.example.logviewrkt.api

import android.telecom.Call
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

class EcosResponse {
    //json 최상위 객체에 연관되는 클래스
    lateinit var StatisticSearch: StatisticSearchResponse
}