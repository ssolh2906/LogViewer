package com.example.logviewrkt

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.api.EcosFetchr
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln


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
        val xAxis :XAxis = chart.xAxis
        xAxis.setValueFormatter(AxisDateFormatter())

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
        var from1970:Float
        for (item in indexItems)
        {
            from1970 = dateToInterval(item.time)
            indexVals.add(Entry(
                from1970,
                (Math.log(item.dataValue.toDouble())/Math.log(10.0)).toFloat()))
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

    private class AxisDateFormatter :IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {

            val format : SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
            val outFormat : SimpleDateFormat = SimpleDateFormat("yy-MM-dd")
            val stdDate : Date = format.parse("19700101")
            val cal = Calendar.getInstance()
            cal.time = stdDate
            cal.add(Calendar.DATE, value.toInt())

            return outFormat.format(cal.time)
        }

    }




}