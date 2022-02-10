package com.example.logviewrkt.screens.logViewer

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
import com.example.logviewrkt.R
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

// User Input
// Update UI
class LogViewerFragment : Fragment() {

    private lateinit var logViewerViewModel: LogViewerViewModel
    private lateinit var btnRequest:Button
    private lateinit var textView: TextView
    private lateinit var fromDate : TextView
    private lateinit var toDate : TextView
    private lateinit var switchLog : Switch
    lateinit var nameOfIndex : String
    lateinit var textNameOfIndex : TextView
    lateinit var  chart : LineChart



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        nameOfIndex = arguments?.getString("name of index").toString()
        logViewerViewModel = ViewModelProvider(this).get(LogViewerViewModel::class.java)
        // fragment 최초생성시에는 요청이실행되어 뷰모델이새로생성되고, 추후 다시 호출되면 이전에 생성 된 뷰모델 사용
    } // on Create

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?) : View
    {
        val view = inflater.inflate(R.layout.fragment_log_viewer, container, false)

        // Binding
        btnRequest = view.findViewById(R.id.btn_request)
        textView = view.findViewById(R.id.text_view)
        chart = view.findViewById(R.id.chart)
        fromDate = view.findViewById(R.id.from_date)
        toDate = view.findViewById(R.id.to_date)
        switchLog = view.findViewById(R.id.switch_log)
        textNameOfIndex = view.findViewById(R.id.text_nameOfIndex)


        // Update Screen
        textNameOfIndex.text = nameOfIndex

        // Set Chart
        initEmptyChart()

        // Set Listeners
        setListeners()


        return view
    } // onCreateView


    //프래그먼트뷰가 생성될 때 라이브데이터 변경을 관찰하도록 함
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }


    /**
     *  private functions
     */

    private fun initEmptyChart(){
        logViewerViewModel.setEmptyChart(chart)
    }

    private fun setListeners() {
        btnRequest.setOnClickListener{ v:View -> onRequestButtonClick(v)}
    }

    private fun onRequestButtonClick(v : View){
        updateLiveData(fromDate, toDate)
        observeLiveData()
        //이미 차트는 업데이트 아닌 데이터 넘어옴
    }

    private fun updateLiveData(fromDate:TextView, ToDate: TextView) {
        logViewerViewModel.updateLivedata(fromDate.text.toString() , toDate.text.toString(), nameOfIndex)
    }

    private fun observeLiveData() {
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

} // logviewer fragment