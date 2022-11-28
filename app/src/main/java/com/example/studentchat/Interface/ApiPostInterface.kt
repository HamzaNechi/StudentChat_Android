package com.example.studentchat.Interface

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiPostInterface {



    @GET(/* value = */ "post")
    fun getAllPost(): Call<ListPost>


    /**@Multipart
    @POST("post")
    fun addPost(@Part image: MultipartBody.Part,
                @Part("description") description:RequestBody,
                @Part("user") user:RequestBody): Call<ServerResponse>*/


    @Multipart
    @POST("post")
    fun addPost(@Part("description") description:RequestBody,
                @Part("user") user:RequestBody): Call<ServerResponse>
}