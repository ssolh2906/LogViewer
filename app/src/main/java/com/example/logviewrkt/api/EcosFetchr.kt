package com.example.logviewrkt.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.logviewrkt.IndexItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "EcosFetchr"

class EcosFetchr {
    private val ecosApi:EcosApi
    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://ecos.bok.or.kr/api/")
            .addConverterFactory(GsonConverterFactory.create()) // Call <String>이므로 String return
            .build()
        // Retrofit instance 생성
        ecosApi = retrofit.create(EcosApi::class.java)
        Log.d(TAG, "EcosFetchr init")
    }

    fun fetchIndex(startYYMM: String, endYYMM:String) : LiveData<List<IndexItem>> {
        // 인터페이스내부의 미구현 함수를 여기서 구현
        // network 요청을 Queue 에 넣고  그결과를 LiveData 로 반환
        val responseLiveData: MutableLiveData<List<IndexItem>> = MutableLiveData()
        val ecosRequest: Call<EcosResponse> = ecosApi.fetchIndex(startYYMM, endYYMM)// 웹요청 나타내는 Call 객체 리턴 ( 실행은 enqueue )

        ecosRequest.enqueue(object : Callback<EcosResponse> {

            override fun onFailure(call: Call<EcosResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

            override fun onResponse(call: Call<EcosResponse>, response: Response<EcosResponse>) {
                Log.d(TAG, "Response recieved" )
                val ecosResponse : EcosResponse? = response.body()
                val statisticSearchResponse : StatisticSearchResponse? = ecosResponse?.StatisticSearch
                var indexItems : List<IndexItem> = statisticSearchResponse?.indexItems?: mutableListOf()
                responseLiveData.value = indexItems
            }
        }) // endqueue, 항상 background 실행

    return responseLiveData // immutable
    }
}