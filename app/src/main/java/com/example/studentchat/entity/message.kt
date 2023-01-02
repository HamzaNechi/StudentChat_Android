package com.example.studentchat.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class message(@SerializedName("_id") @Expose var id:String,
                   @SerializedName("chat_id") @Expose var chat_id:String,
                   @SerializedName("user_id") @Expose var user:User,
                   @SerializedName("content") @Expose var content:String,
                   @SerializedName("date") @Expose var date:Date, )
