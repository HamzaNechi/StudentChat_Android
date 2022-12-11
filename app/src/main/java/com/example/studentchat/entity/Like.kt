package com.example.studentchat.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("post") @Expose val post:String,
    @SerializedName("user") @Expose val user:String)
