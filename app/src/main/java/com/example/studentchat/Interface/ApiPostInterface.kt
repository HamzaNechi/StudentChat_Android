package com.example.studentchat.Interface

import com.example.studentchat.entity.Comment
import com.example.studentchat.entity.Like
import com.example.studentchat.entity.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiPostInterface {

    //login
    @POST("user/signin")
    fun login(@Body body:HashMap<String, String>): Call<User>

    //Retrofit function post

    @GET(/* value = */ "post")
    fun getAllPost(): Call<ListPost>


    @Multipart
    @POST("post")
    fun addPost(@Part image: MultipartBody.Part,
                @Part("description") description:RequestBody,
                @Part("user") user:RequestBody,
                @Part("author") author:RequestBody): Call<ServerResponse>


    @Multipart
    @POST("post")
    fun addPostSansImage(@Part("description") description:RequestBody,
                @Part("user") user:RequestBody,
                @Part("author") author:RequestBody): Call<ServerResponse>


    @GET(/* value = */ "post/delete/{id_post}")
    fun deletePost(@Path("id_post") id_post:String?): Call<ServerResponse>
    /****************************Partage post***************************************/
    @POST("post/share")
    fun sharePost(@Body body:HashMap<String, String>): Call<ServerResponse>
    @GET("user/{id}")
    fun getAuthor(@Path("id") id:String?):Call<User>
    /************************ End partage post************************************/

    /***************************************Ret consomation like***********************************/
    @GET("like/{post_id}")
    fun AskisLike(@Path("post_id") post_id:String?):Call<ArrayList<Like>>


    @POST("like")
    fun addLike(@Body like:Like): Call<ServerResponse>

    @POST("like/delete")
    fun deleteLike(@Body like:Like): Call<ServerResponse>
    /***************************************End consommation like**********************************/



    /***************************** Retrofit consommation commentaire *****************************/
    @GET(/* value = */ "comment/{post_id_comment}")
    fun getComments(@Path("post_id_comment") id_post:String?): Call<ArrayList<Comment>>

    @POST("comment")
    fun addComment(@Body body:HashMap<String, String>): Call<ServerResponse>

    @GET(/* value = */ "comment/delete/{id_comment}")
    fun deleteComment(@Path("id_comment") id_comment:String?): Call<ServerResponse>
    /************************** end consommation commentaire **********************************/


    companion object Connection {
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.11:9090/")
            .build()
            .create(ApiPostInterface::class.java)
    }
}