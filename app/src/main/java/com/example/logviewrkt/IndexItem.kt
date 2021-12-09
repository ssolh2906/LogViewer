package com.example.logviewrkt

import com.google.gson.annotations.SerializedName

// Model

class IndexItem {
    @SerializedName("DATA_VALUE") var dataValue: String = ""
    @SerializedName("TIME") var time:String = ""
}