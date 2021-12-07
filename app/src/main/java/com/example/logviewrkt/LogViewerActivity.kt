package com.example.logviewrkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LogViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, LogViewerFragment.newInstance())
                .commit()
        }

    } // on create
}