package com.example.studentchat.Interface

import com.google.gson.annotations.SerializedName




class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    var success = false

    @SerializedName("message")
    var message: String? = null
}