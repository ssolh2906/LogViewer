package com.example.logviewrkt.api.Ecos.monthly

import com.example.logviewrkt.IndexItem
import com.google.gson.annotations.SerializedName

class StatisticSearchResponse {
    @SerializedName("row")
    lateinit var indexItems: List<IndexItem>
    // "row" 배열에 저장 된 IndexItem 객체 추가
}