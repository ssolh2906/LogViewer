package com.example.logviewrkt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logviewrkt.api.EcosApi
import com.example.logviewrkt.api.EcosFetchr
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val TAG = "LogViewerFragment"

class LogViewerFragment : Fragment() {

    private lateinit var logViewerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ecosLiveData: LiveData<List<IndexItem>> = EcosFetchr().fetchIndex()
        ecosLiveData.observe(
            this,
            Observer { indexItems ->
            Log.d(TAG, "Response received: $indexItems")
            }
        )

    } // on Create



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_log_viewer, container, false)

        logViewerView = view.findViewById(R.id.log_viewer_view)
        logViewerView.layoutManager = GridLayoutManager(context, 3)

        return view
    } // onCreateView

    companion object {
        fun newInstance() = LogViewerFragment()
    }



} // logviewer fragment