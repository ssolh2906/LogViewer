package com.example.logviewrkt

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

private const val TAG: String = "LogViewerActivity"

class LogViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        val intent : Intent = intent
        val bundle: Bundle = Bundle()
        val nameOfIndex = intent.getStringExtra("nameOfIndex")
        bundle.putString("name of index",nameOfIndex)
        Log.d(TAG, "NAME of Index $nameOfIndex")
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