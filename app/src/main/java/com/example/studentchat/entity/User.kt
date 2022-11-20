package com.example.studentchat.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") @Expose var id:String,
    @SerializedName("username") @Expose var username:String,
    @SerializedName("password") @Expose var password:String,
    @SerializedName("role") @Expose var role:String,
    @SerializedName("email") @Expose var email:String,
    @SerializedName("image") @Expose var image:String,
)