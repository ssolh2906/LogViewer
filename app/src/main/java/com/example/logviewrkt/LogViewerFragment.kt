package com.example.logviewrkt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text


private const val TAG = "LogViewerFragment"

class LogViewerFragment : Fragment() {

    private lateinit var logViewerViewModel: LogViewerViewModel
    private lateinit var logViewerRecyclerView: RecyclerView
    private lateinit var btnRequest:Button
    private lateinit var textView: TextView

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

        logViewerRecyclerView = view.findViewById(R.id.log_viewer_view_recycler_view)
        logViewerRecyclerView.layoutManager = GridLayoutManager(context, 3)

        btnRequest = view.findViewById(R.id.btn_request)
        textView = view.findViewById(R.id.text_view)


        return view
    } // onCreateView




    //프래그먼트뷰가 생성될 때 라이브데이터 변경을 관찰하도록 함
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRequest.setOnClickListener { v:View ->
            textView.text = "Button clicked"
            logViewerViewModel.indexItemLiveData.observe(
                viewLifecycleOwner,
                Observer { indexItems ->
                    Log.d(TAG, "Have index items from view model $indexItems")
                    // View 내용변경도 여기서 이루어짐, 응답 데이터 변경 시 어댑터에 전달
                    logViewerRecyclerView.adapter = IndexAdapter(indexItems)
                }
            )

        }
    }



    //ViewHolder Subclass 추가
    private class IndexHolder(itemTextView: TextView) : RecyclerView.ViewHolder(itemTextView)
    {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    // RecyclerView.Adapter  의 Subclass
    private class IndexAdapter(private val indexItems: List<IndexItem>) : RecyclerView.Adapter<IndexHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexHolder {
            val textView = TextView(parent.context)
            return IndexHolder(textView)
        }

        override fun getItemCount(): Int = indexItems.size

        override fun onBindViewHolder(holder: IndexHolder, position: Int) {
            val indexItem = indexItems[position]
            holder.bindTitle(indexItem.dataValue)
        }
    }

    companion object {
    fun newInstance() = LogViewerFragment()
    }


} // logviewer fragment