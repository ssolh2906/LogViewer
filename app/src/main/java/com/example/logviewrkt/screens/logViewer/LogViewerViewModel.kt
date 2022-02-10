package com.example.logviewrkt.screens.logViewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.logviewrkt.IndexItem
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

// Business Logic
class LogViewerViewModel :ViewModel()
{

    var data : LineData? = null
    lateinit var indexItemLiveData: LiveData<List<IndexItem>>     // live data 참조
    lateinit var lineDataSet : LineDataSet
    lateinit var dataSets :  ArrayList<ILineDataSet>


    init {
        // view model 최초 실행 시에만 웹 요청하고 여기에 데이터 저장
        updateLivedata("202101", "202111", "KOSPI")
    }

    /**
     * public function , Called by Fragment
     */
    fun setEmptyChart(chart : LineChart) {
        chart.setNoDataText("기간을 입력하신 후 요청하기/새로고침 버튼을 눌러주세요")
        chart.xAxis.valueFormatter = AxisDateFormatter()
    }

    fun updateLivedata(fromDate:String, toDate:String, nameOfIndex : String): Unit
    {
        // Ecos Daily
        //indexItemLiveData = EcosFetchr().fetchIndex(fromDate, toDate, nameOfIndex)
        // Ecos Monthly
        indexItemLiveData = com.example.logviewrkt.api.Ecos.monthly.EcosFetchr().fetchIndex(fromDate, toDate, nameOfIndex)
    }

    fun updateChart(indexItems: List<IndexItem>,chart: LineChart, isLog : Boolean, nameOfIndex: String):Unit {
        data = getLineData(indexItems, isLog, nameOfIndex)
        chart.data = data
        chart.invalidate()
    }

    // private function, Called by ViewModel
    private fun getLineData(indexItems: List<IndexItem>, isLog: Boolean, nameOfIndex: String) :LineData {
        lineDataSet = LineDataSet(indexValues(indexItems, isLog), nameOfIndex)

        lineDataSet.setDrawCircles(false)

        dataSets = ArrayList()

        dataSets.add(lineDataSet)

        data= LineData(dataSets);

        return data as LineData
    }


    //
    enum class Period {
        DAILY, MONTHLY
    }

    private fun dailyOrMonthly() : Period
    {
        return Period.MONTHLY
    }
    // DATE
    private fun indexValues(indexItems: List<IndexItem>, isLog: Boolean, logBase : Float = 7f) : ArrayList<Entry>
    {
        var indexVals : ArrayList<Entry> = ArrayList()

        if (isLog) {
            for (item in indexItems)
                indexVals.add(getLogEntry(item,dailyOrMonthly(),logBase))
        } else { // is NOT Log
            for (item in indexItems)
                indexVals.add(getNonLogEntry(item, dailyOrMonthly()))
        }

        return indexVals
    }

    private fun getLogEntry(item: IndexItem, period: Period, logBase: Float, ): Entry {
        val from1970 = dateToInterval(getTimeStr(item, period))

        return Entry(
            from1970,
            (ln(item.dataValue.toDouble()) / ln(logBase)).toFloat()
        )
    }

    private fun getNonLogEntry(item: IndexItem, period: Period): Entry {
        val from1970 = dateToInterval(getTimeStr(item, period))

        return Entry(
            from1970,
            (item.dataValue.toFloat())
        )
    }

    private fun getTimeStr(item: IndexItem, period: Period) : String{
        return if (period == Period.DAILY) item.time else item.time + "01"
    }


    private class AxisDateFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            Log.d(TAG, "getFormattedValue")

            val format : SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
            val outFormat : SimpleDateFormat = SimpleDateFormat("yyMM")
            val stdDate : Date = format.parse("19700101")

            val cal = Calendar.getInstance()
            cal.time = stdDate
            cal.add(Calendar.DATE, value.toInt())

            return outFormat.format(cal.time)
        }
    }

    private fun dateToInterval(time:String) : Float
    {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val stdDate = dateFormat.parse("19700101").time
        val targetDate = dateFormat.parse(time).time

        return ((targetDate - stdDate) / (24*60*60*1000)).toFloat()

    }

}