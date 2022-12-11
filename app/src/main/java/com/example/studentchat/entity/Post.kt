package com.example.studentchat.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Post(
    @SerializedName("_id") @Expose val id:String,
    @SerializedName("image") @Expose val image:String,
    @SerializedName("description") @Expose val description:String,
    @SerializedName("date") @Expose val date:Date,
    @SerializedName("user") @Expose val u:User,
    @SerializedName("author") @Expose val author:String
    )


