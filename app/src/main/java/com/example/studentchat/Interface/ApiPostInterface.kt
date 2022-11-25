package com.example.studentchat.Interface

import retrofit2.Call
import retrofit2.http.*


interface ApiPostInterface {



    @GET(/* value = */ "post")
    fun getAllPost(): Call<ListPost>


    /*@Multipart
    @POST("post")
    fun addPost(@Part image: Part): Call<ServerResponse>*/



}