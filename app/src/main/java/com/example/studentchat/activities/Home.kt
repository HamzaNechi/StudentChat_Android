package com.example.studentchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.R
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val posts=findViewById<RecyclerView>(R.id.recycler_post);
        posts.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        //Retrofit
        val retrofitBuilder=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.15:9090/")
            .build()
            .create(ApiPostInterface::class.java)

        val retrofitData=retrofitBuilder.getAllPost()
        Log.e("retrofitData",retrofitData.toString());
        retrofitData.enqueue(object :Callback<ListPost>{
            override fun onResponse(call: Call<ListPost>, response: Response<ListPost>) {
                val lp:ListPost= response.body()!!
                val listPosts:ArrayList<Post> = lp.posts.toCollection(kotlin.collections.ArrayList())
                posts.adapter=PostAdapter(listPosts)
            }

            override fun onFailure(call: Call<ListPost>, t: Throwable) {
                Log.e("onFailure",t.toString())
            }
        })
        //End Retrofit





    }


}