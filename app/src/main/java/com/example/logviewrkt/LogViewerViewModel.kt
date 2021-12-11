package com.example.logviewrkt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.api.EcosFetchr
import com.github.mikephil.charting.data.Entry

class LogViewerViewModel :ViewModel(){

    // live data 참조
    val indexItemLiveData: LiveData<List<IndexItem>>

    init {
        indexItemLiveData = EcosFetchr().fetchIndex("202001", "202110")
        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장



    }


}