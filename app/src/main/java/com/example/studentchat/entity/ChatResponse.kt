package com.example.studentchat.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ChatResponse(
    @SerializedName("_id") @Expose var id:String,
    @SerializedName("chat_id") @Expose var chat_id:Chat,
    @SerializedName("user_id") @Expose var user:User,
    @SerializedName("content") @Expose var content:String,
    @SerializedName("date") @Expose var date: Date,
)