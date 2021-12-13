package com.example.logviewrkt

import android.os.Bundle
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


private const val TAG = "LogViewerFragment"

class LogViewerFragment : Fragment() {

    private lateinit var logViewerViewModel: LogViewerViewModel
    private lateinit var btnRequest:Button
    private lateinit var textView: TextView
    private lateinit var chart : LineChart
    private lateinit var fromDate : TextView
    private lateinit var toDate : TextView
    private lateinit var switchLog : Switch
    private var isLog : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logViewerViewModel =
            ViewModelProvider(this).get(LogViewerViewModel::class.java)
        // fragment 최초생성시에는 요청이실행되어 뷰모델이새로생성되고, 추후 다시 호출되면 이전에 생성 된 뷰모델 사용
    } // on Create

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_log_viewer, container, false)

        //logViewerRecyclerView = view.findViewById(R.id.log_viewer_view_recycler_view)
        //logViewerRecyclerView.layoutManager = GridLayoutManager(context, 3)

        btnRequest = view.findViewById(R.id.btn_request)
        textView = view.findViewById(R.id.text_view)
        chart = view.findViewById(R.id.chart)
        fromDate = view.findViewById(R.id.from_date)
        toDate = view.findViewById(R.id.to_date)
        switchLog = view.findViewById(R.id.switch_log)

        return view
    } // onCreateView




    //프래그먼트뷰가 생성될 때 라이브데이터 변경을 관찰하도록 함
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRequest.setOnClickListener { v:View ->

            logViewerViewModel.indexItemLiveData.observe(
                viewLifecycleOwner,
                Observer { indexItems ->
                    Log.d(TAG, "Have index items from view model {$indexItems}")
                    // View 내용변경도 여기서 이루어짐, 응답 데이터 변경 시 어댑터에 전달
                    //logViewerRecyclerView.adapter = IndexAdapter(indexItems)
                    logViewerViewModel.updateLivedata(fromDate.text.toString() , toDate.text.toString())
                    chart = logViewerViewModel.updateChart(indexItems, chart, switchLog.isChecked)

                }
            )

        }
    }


    companion object {
    fun newInstance() = LogViewerFragment()
    }







} // logviewer fragment