package com.example.logviewrkt

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


class LogViewerViewModel :ViewModel(){

    // live data 참조
    lateinit var indexItemLiveData: LiveData<List<IndexItem>>
    lateinit var data :LineData
    lateinit var nameOfIndex: String


    init {

        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장
        //indexItemLiveData = EcosFetchr().fetchIndex("202101", "202111", "KOSPI")

    }

    fun updateLivedata(fromDate:String, toDate:String, nameOfIndex : String): Unit
    {

        indexItemLiveData = EcosFetchr().fetchIndex(fromDate, toDate, nameOfIndex)
    }

    fun updateChart(indexItems: List<IndexItem>,chart: LineChart, isLog : Boolean, nameOfIndex: String):LineChart {
        val data : LineData = getLineData(indexItems, isLog)
        chart.data = data
        chart.xAxis.valueFormatter = AxisDateFormatter()
        chart.invalidate()

        return chart
    }


    private fun getLineData(indexItems: List<IndexItem>, isLog: Boolean) :LineData {
        var lineDataSet : LineDataSet = LineDataSet(indexValues(indexItems, isLog), "KOSPI")
        lineDataSet.setDrawCircles(false)
        var dataSets : ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)
        var data: LineData = LineData(dataSets);

        return data
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