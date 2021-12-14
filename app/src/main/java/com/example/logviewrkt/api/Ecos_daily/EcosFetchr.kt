package com.example.logviewrkt.api.Ecos_daily

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.logviewrkt.IndexItem
import com.example.logviewrkt.LogViewerViewModel
import com.example.logviewrkt.api.Ecos_daily.EcosApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

private const val TAG = "EcosFetchr"

class EcosFetchr {
    private val ecosApi: EcosApi
    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://ecos.bok.or.kr/api/")
            .addConverterFactory(GsonConverterFactory.create()) // Call <String>이므로 String return
            .build()
        // Retrofit instance 생성
        ecosApi = retrofit.create(EcosApi::class.java)
        Log.d(TAG, "EcosFetchr init")
    }


    fun fetchIndex(startYYMM: String, endYYMM:String, nameOfIndex:String) : LiveData<List<IndexItem>> {
        // 인터페이스내부의 미구현 함수를 여기서 구현
        // network 요청을 Queue 에 넣고  그결과를 LiveData 로 반환
        val responseLiveData: MutableLiveData<List<IndexItem>> = MutableLiveData()
        val endIndex :String = getEndIndex(startYYMM, endYYMM)
        val indexID : String = getIndexID(nameOfIndex)
        val ecosRequest: Call<EcosResponse> = ecosApi.fetchIndex(startYYMM, endYYMM, endIndex, indexID)// 웹요청 나타내는 Call 객체 리턴 ( 실행은 enqueue )


        ecosRequest.enqueue(object : Callback<EcosResponse> {

            override fun onFailure(call: Call<EcosResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

            override fun onResponse(call: Call<EcosResponse>, response: Response<EcosResponse>) {
                Log.d(TAG, "Response recieved")
                val ecosResponse : EcosResponse? = response.body()
                val statisticSearchResponse : StatisticSearchResponse? = ecosResponse?.StatisticSearch
                var indexItems = statisticSearchResponse?.indexItems?: mutableListOf()
                responseLiveData.value = indexItems
            }

        }) // endqueue, 항상 background 실행


        return responseLiveData // immutable
    }

    private fun getEndIndex(startYYMM: String, endYYMM: String):String
    {
        val dateFormat = SimpleDateFormat("yyyyMMdd")

        val startDate = dateFormat.parse((startYYMM+"01")).time
        val endDate = dateFormat.parse((endYYMM+"01")).time
        val endIndex  = ((endDate - startDate) / (24 * 60 * 60 * 1000)).toInt().toString()

        Log.d(TAG, "EndIndex :" + endIndex)

        return endIndex
    }

    private fun getIndexID(nameOfIndex: String) : String
    {
        when(nameOfIndex)
        {
            "KOSPI" ->
            {
                return "0001000"
            }
            "KOSDAQ" ->
            {
                return "0089000"
            }

        }
        return "0001000"
    }


}