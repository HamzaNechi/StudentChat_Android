package com.example.studentchat.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.R
import com.example.studentchat.activities.Home
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DisplayPost: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_display_post, container, false)





        val posts=view.findViewById<RecyclerView>(R.id.recycler_post);
        posts.layoutManager= LinearLayoutManager(this.context?.let { it}, RecyclerView.VERTICAL,false);
        //Retrofit
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.11:9090/")
            .build()
            .create(ApiPostInterface::class.java)

        val retrofitData=retrofitBuilder.getAllPost()
        Log.e("retrofitData",retrofitData.toString());
        retrofitData.enqueue(object : Callback<ListPost> {
            override fun onResponse(call: Call<ListPost>, response: Response<ListPost>) {
                if (response.isSuccessful){
                   // Toast.makeText(context,response.code().toString(),Toast.LENGTH_LONG).show()
                    val lp: ListPost = response.body()!!
                    val listPosts:ArrayList<Post> = lp.posts.toCollection(kotlin.collections.ArrayList())
                    posts.adapter= PostAdapter(listPosts)

                }

            }

            override fun onFailure(call: Call<ListPost>, t: Throwable) {
                Log.e("onFailure",t.toString())
            }
        })
        //End Retrofit
        return view
    }
}