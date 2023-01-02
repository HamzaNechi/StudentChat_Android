package com.example.studentchat.Interface

import com.example.studentchat.entity.*
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


    @POST(/* value = */ "user/fetch")
    fun FetchUser(@Body body:HashMap<String, String>): Call<ArrayList<User>>


    @PATCH(/* value = */ "user/logout")
    fun Logout(@Body body:HashMap<String, String>): Call<ServerResponse>

    //Retrofit function post

    @GET(/* value = */ "post")
    fun getAllPost(): Call<ListPost>

    /*********************** post ****************************/
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

    //update post avec et sans image
    @Multipart
    @POST("post/update")
    fun updatePost(@Part image: MultipartBody.Part,
                   @Part("description") description:RequestBody,
                   @Part("id") id_post:RequestBody): Call<ServerResponse>



    @POST("post/update")
    fun updatePostSansImage(@Body map:HashMap<String,String>): Call<ServerResponse>
    //end update post




    //cherhcer post
    @POST("post/fetch")
    fun serchPost(@Body map:HashMap<String,String>): Call<ListPost>
    /************************** end post ********************************/




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



    /************************ Consommation invitation ***********************************/
    @GET("invitations/{currentUser}")
    fun getInvitationEnAttente(@Path("currentUser") currentUser:String):Call<ArrayList<Invitations>>

    @POST("invitations/accept")
    fun acceptInvitation(@Body body: HashMap<String, String>):Call<ServerResponse>

    @POST("invitations/refuse")
    fun refuseInvitation(@Body body: HashMap<String, String>):Call<ServerResponse>
    /*************************end consommation invitation******************************/



    /******************** Consommation amis ***********************************/
    @GET("amis/{currentUser}")
    fun getAllAmis(@Path("currentUser") currentUser:String):Call<ArrayList<User>>

    @POST("amis/delete")
    fun deleteAmis(@Body body: HashMap<String, String>):Call<ServerResponse>
    /******************* end Consomation amis *********************************/



    /********************* consommation chat room***************************/
    @POST("/chat")
    fun getOrCreateRoomPrive(@Body body:HashMap<String,String>):Call<String>


    //add room chat groupe
    @Multipart
    @POST("chat/add_room")
    fun addRoomChat(@Part image: MultipartBody.Part,
                @Part("nom") nom:RequestBody,
                @Part("admin") admin:RequestBody,
                @Part("arrUser") arrUser:ArrayList<RequestBody>): Call<Chat>


    @POST("chat/groupe")
    fun getAllGroupe(@Body map: HashMap<String, String>):Call<ArrayList<Chat>>

    @PUT("chat/quitter_groupe")
    fun quitterGroupe(@Body map:HashMap<String,String>):Call<ServerResponse>
    /******************** end consommation chat room*************************/


    /********************** consommation message ****************************/
    @GET("/message/{chat_id}")
    fun getMsgParChat(@Path("chat_id") chat_id:String):Call<ArrayList<message>>

    @POST("/message/send")
    fun sendMessage(@Body body:HashMap<String,String>):Call<ServerResponse>


    @POST("/message/msg_user")
    fun getMessagesUser(@Body body:HashMap<String,String>):Call<ArrayList<ChatResponse>>
    /********************** end consommation message ***********************/


    companion object Connection {
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.11:9090/")
            .build()
            .create(ApiPostInterface::class.java)
    }
}