package com.example.logviewrkt

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LogViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        val intent : Intent = intent
        var bundle: Bundle = Bundle()
        bundle.putString("name of index",intent.getStringExtra("nameOfIndex"))
        val logViewerFragment = LogViewerFragment.newInstance()
        logViewerFragment.arguments = bundle

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, logViewerFragment)
                .commit()
        }



    } // on create
}