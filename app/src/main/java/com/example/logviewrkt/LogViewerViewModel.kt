package com.example.logviewrkt

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.api.EcosFetchr
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class LogViewerViewModel :ViewModel(){

    // live data 참조
    var indexItemLiveData: LiveData<List<IndexItem>>
    lateinit var data :LineData

    init {
        indexItemLiveData = EcosFetchr().fetchIndex("202101", "202111")
        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장
    }

    public fun updateLivedata(fromDate:String, toDate:String): Unit
    {
        indexItemLiveData = EcosFetchr().fetchIndex(fromDate, toDate)
    }

    public fun updateChart(indexItems: List<IndexItem>,chart: LineChart):LineChart {
        val data : LineData = getLineData(indexItems)
        chart.data = data
        chart.invalidate()

        return chart
    }


    private fun getLineData(indexItems: List<IndexItem>) :LineData {
        var lineDataSet : LineDataSet = LineDataSet(indexValues(indexItems), "Linear Data Set")
        var dataSets : ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)
        var data: LineData = LineData(dataSets);

        return data
    }

    private fun indexValues(indexItems: List<IndexItem>) : ArrayList<Entry>
    {
        var indexVals : ArrayList<Entry> = ArrayList<Entry>()

        for (item in indexItems)
        {
            indexVals.add(Entry(item.time.toFloat(), item.dataValue.toFloat()))
        }

        return indexVals
    }


}