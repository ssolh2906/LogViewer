package com.example.logviewrkt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.api.EcosFetchr

class LogViewerViewModel :ViewModel(){

    // live data 참조
    val indexItemLiveData: LiveData<List<IndexItem>>

    init {
        indexItemLiveData = EcosFetchr().fetchIndex()
        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장
    }
}