package com.example.logviewrkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.logviewrkt.screens.logViewer.LogViewerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mOnClick(v :View) {

        var intent : Intent
        intent = Intent(this, LogViewerActivity::class.java)

            when(v.id) {
                R.id.btn_kospi -> {
                    intent.putExtra("nameOfIndex", "KOSPI")
                }
                R.id.btn_kosdaq -> {
                    intent.putExtra("nameOfIndex", "KOSDAQ")
                }
            }
        startActivity(intent)
    }


}