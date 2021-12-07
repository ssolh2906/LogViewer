package com.example.logviewrkt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logviewrkt.api.EcosApi
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

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://ecos.bok.or.kr/api/")
            .addConverterFactory(ScalarsConverterFactory.create()) // Call <String>이므로 String return
            .build()
        // Retrofit instance 생성

        val ecosApi: EcosApi = retrofit.create(EcosApi::class.java)

        val ecosHomePageRequest: Call<String> = ecosApi.fetchContents() // 웹요청 나타내는 Call 객체 리턴 ( 실행은 enqueue )

        ecosHomePageRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch", t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Response recieved: ${response.body()}" )
            }
        }) // endqueue, 항상 background 실행


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