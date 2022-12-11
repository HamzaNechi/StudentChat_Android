package com.example.studentchat.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.R
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayPost: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_display_post, container, false)
        val posts=view.findViewById<RecyclerView>(R.id.recycler_post);
        posts.layoutManager= LinearLayoutManager(this.context?.let { it}, RecyclerView.VERTICAL,false);
        RefreshRecycler(posts,view.context)


        //on refresh layout refresh recycler
        val swipe=view.findViewById<SwipeRefreshLayout>(R.id.swipe_display)
        swipe.setOnRefreshListener {
            RefreshRecycler(posts, view.context)
            if(swipe.isRefreshing){
                swipe.setRefreshing(false);
            }
        }
        return view
    }


    fun RefreshRecycler(list: RecyclerView, context: Context) {
        val retrofitBuilder=ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getAllPost()
        retrofitData.enqueue(object : Callback<ListPost> {
            override fun onResponse(call: Call<ListPost>, response: Response<ListPost>) {
                if (response.isSuccessful){
                    val lp: ListPost = response.body()!!
                    val listPosts:ArrayList<Post> = lp.posts.toCollection(kotlin.collections.ArrayList())
                    list.adapter= PostAdapter(listPosts,context)
                    list.scrollToPosition(0);
                }

            }

            override fun onFailure(call: Call<ListPost>, t: Throwable) {
                Log.e("onFailure",t.toString())
            }
        })
    }
}