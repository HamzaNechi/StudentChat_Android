package com.example.studentchat.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.R
import com.example.studentchat.adapters.MessageListAdapter
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.ChatResponse
import com.example.studentchat.entity.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiscussionFragment: Fragment() {
    lateinit var list: ArrayList<ChatResponse>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_discussion, container, false);
        val recyclerMsg=view.findViewById<RecyclerView>(R.id.chat_recycler);
        recyclerMsg.layoutManager=LinearLayoutManager(this.context?.let { it},RecyclerView.VERTICAL,false);


        val search=view.findViewById<SearchView>(R.id.search_msg)
        val swipe=view.findViewById<SwipeRefreshLayout>(R.id.swipe_msg)
        //get all msg
        //get user connected
        val sharedPref= this.activity?.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        val currentUser_id = sharedPref?.getString("_id","default value").toString()
        val map=HashMap<String,String>();
        map.put("user_id",currentUser_id)
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getMessagesUser(map)
        retrofitData.enqueue(object : Callback<ArrayList<ChatResponse>> {
            override fun onResponse(call: Call<ArrayList<ChatResponse>>, response: Response<ArrayList<ChatResponse>>) {
                if (response.isSuccessful){
                    list= response.body()!!
                    val arrChat_id=ArrayList<String>();
                    for (item in list){
                        if(!arrChat_id.contains(item.chat_id._id)){
                            arrChat_id.add(item.chat_id._id)
                        }
                    }
                    for (i in arrChat_id){
                        Log.e("chat id message",i)
                    }
                    recyclerMsg.adapter=MessageListAdapter(list,arrChat_id);
                }else{
                    Log.e("faile get chat_id",response.body().toString())
                }

            }

            override fun onFailure(call: Call<ArrayList<ChatResponse>>, t: Throwable) {
                Log.e("onFailure_chat_id",t.toString())
            }
        })



        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(queryT: String?): Boolean {
                return true;
            }

            override fun onQueryTextChange(queryT: String?): Boolean {
                return true
            }

        })

        return view
    }


}