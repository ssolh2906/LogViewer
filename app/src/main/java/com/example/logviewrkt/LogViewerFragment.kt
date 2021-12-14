package com.example.logviewrkt

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "LogViewerFragment"

class LogViewerFragment : Fragment() {

    private lateinit var logViewerViewModel: LogViewerViewModel
    private lateinit var btnRequest:Button
    private lateinit var textView: TextView
    private lateinit var chart : LineChart
    private lateinit var fromDate : TextView
    private lateinit var toDate : TextView
    private lateinit var switchLog : Switch
    lateinit var nameOfIndex : String
    lateinit var textNameOfIndex : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameOfIndex = arguments?.getString("name of index").toString()
        logViewerViewModel =
            ViewModelProvider(this).get(LogViewerViewModel::class.java)
        // fragment 최초생성시에는 요청이실행되어 뷰모델이새로생성되고, 추후 다시 호출되면 이전에 생성 된 뷰모델 사용


    } // on Create

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View
    {
        val view = inflater.inflate(R.layout.fragment_log_viewer, container, false)

        //logViewerRecyclerView = view.findViewById(R.id.log_viewer_view_recycler_view)
        //logViewerRecyclerView.layoutManager = GridLayoutManager(context, 3)

        btnRequest = view.findViewById(R.id.btn_request)
        textView = view.findViewById(R.id.text_view)
        chart = view.findViewById(R.id.chart)
        chart.setNoDataText("기간을 입력하신 후 요청하기/새로고침 버튼을 눌러주세요")
        chart.xAxis.valueFormatter = AxisDateFormatter()
        fromDate = view.findViewById(R.id.from_date)
        toDate = view.findViewById(R.id.to_date)
        switchLog = view.findViewById(R.id.switch_log)
        textNameOfIndex = view.findViewById(R.id.text_nameOfIndex)
        textNameOfIndex.text = nameOfIndex


        btnRequest.setOnClickListener { v:View ->
            logViewerViewModel.updateLivedata(fromDate.text.toString() , toDate.text.toString(), nameOfIndex)
            logViewerViewModel.indexItemLiveData.observe(
                viewLifecycleOwner,
                Observer { indexItems ->
                    Log.d(TAG, "Change Observed,Have index items from view model {$indexItems}")
                    // View 내용변경도 여기서 이루어짐, 응답 데이터 변경 시 전달
                    logViewerViewModel.updateChart(indexItems, chart, switchLog.isChecked, nameOfIndex)
                }
            )
            //이미 차트는 업데이트 아닌 데이터 넘어옴

        }// btn click

        return view
    } // onCreateView




    //프래그먼트뷰가 생성될 때 라이브데이터 변경을 관찰하도록 함
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logViewerViewModel.indexItemLiveData.observe(
            viewLifecycleOwner,
            Observer { indexItems ->
                Log.d(TAG, "Change Observed,Have index items from view model {$indexItems}")
                // View 내용변경도 여기서 이루어짐, 응답 데이터 변경 시 전달
                logViewerViewModel.updateChart(indexItems, chart, switchLog.isChecked, nameOfIndex)
            }
        )


    }




    companion object {
    fun newInstance() = LogViewerFragment()
    }


    private class AxisDateFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            Log.d(TAG, "#11")

            val format : SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
            val outFormat : SimpleDateFormat = SimpleDateFormat("yy-MM-dd")
            val stdDate : Date = format.parse("19700101")
            val cal = Calendar.getInstance()
            cal.time = stdDate
            cal.add(Calendar.DATE, value.toInt())

            return outFormat.format(cal.time)
        }

    }






} // logviewer fragment