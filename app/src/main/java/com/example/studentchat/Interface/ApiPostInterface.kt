package com.example.studentchat.Interface

import com.example.studentchat.entity.Post
import retrofit2.Call
import retrofit2.http.GET

interface ApiPostInterface {



    @GET(/* value = */ "post")
    fun getAllPost(): Call<ListPost>
}