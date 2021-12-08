package com.example.logviewrkt.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "EcosFetchr"

class EcosFetchr {
    private val ecosApi:EcosApi
    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://ecos.bok.or.kr/api/")
            .addConverterFactory(ScalarsConverterFactory.create()) // Call <String>이므로 String return
            .build()
        // Retrofit instance 생성
        ecosApi = retrofit.create(EcosApi::class.java)
    }

    fun fetchIndex() : LiveData<String> {    // network 요청을 Queue 에 넣고  그결과를 LiveData 로 반환
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val ecosRequest: Call<String> = ecosApi.fetchIndex()// 웹요청 나타내는 Call 객체 리턴 ( 실행은 enqueue )

        ecosRequest.enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Response recieved: ${response.body()}" )
                responseLiveData.value = response.body()
            }
        }) // endqueue, 항상 background 실행

    return responseLiveData // immutable
    }
}