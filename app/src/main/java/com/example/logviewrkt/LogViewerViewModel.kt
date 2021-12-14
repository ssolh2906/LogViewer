package com.example.logviewrkt

import android.nfc.Tag
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.api.Ecos_daily.EcosFetchr
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln

private const val TAG = "LogViewerViewModel"

class LogViewerViewModel :ViewModel(){

    // live data 참조

    var indexItemLiveData: LiveData<List<IndexItem>>
    var data : LineData? = null
    lateinit var nameOfIndex: String
    lateinit var lineDataSet : LineDataSet
    lateinit var dataSets :  ArrayList<ILineDataSet>


    init {

        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장
        indexItemLiveData = EcosFetchr().fetchIndex("202101", "202111", "KOSPI")

    }



    fun updateLivedata(fromDate:String, toDate:String, nameOfIndex : String): Unit
    {

        indexItemLiveData = EcosFetchr().fetchIndex(fromDate, toDate, nameOfIndex)

    }

    fun updateChart(indexItems: List<IndexItem>,chart: LineChart, isLog : Boolean, nameOfIndex: String):Unit {
        data = getLineData(indexItems, isLog, nameOfIndex)
        chart.data = data
        chart.invalidate()
    }


    private fun getLineData(indexItems: List<IndexItem>, isLog: Boolean, nameOfIndex: String) :LineData {
        lineDataSet = LineDataSet(indexValues(indexItems, isLog), nameOfIndex)

        lineDataSet.setDrawCircles(false)

        dataSets = ArrayList()

        dataSets.add(lineDataSet)

        data= LineData(dataSets);

        return data as LineData
    }

    private fun indexValues(indexItems: List<IndexItem>, isLog: Boolean) : ArrayList<Entry>
    {
        var indexVals : ArrayList<Entry> = ArrayList<Entry>()
        var from1970:Float
        if (isLog) {
            for (item in indexItems) {
                from1970 = dateToInterval(item.time)
                indexVals.add(
                    Entry(
                        from1970,
                        (ln(item.dataValue.toDouble()) / ln(10f)).toFloat()
                    )
                )
            }
        } else {
            for (item in indexItems) {
                from1970 = dateToInterval(item.time)
                indexVals.add(
                    Entry(
                        from1970,
                        (item.dataValue.toFloat())
                    )
                )
            }

        }
        return indexVals
    }

    private fun dateToInterval(time:String) : Float
    {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val stdDate = dateFormat.parse("19700101").time
        val targetDate = dateFormat.parse(time).time

        return ((targetDate - stdDate) / (24*60*60*1000)).toFloat()

    }




}