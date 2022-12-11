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
import com.example.studentchat.R
import com.example.studentchat.adapters.AmisAdapter
import com.example.studentchat.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Contacts(val ctx:Context) : Fragment() {
    lateinit var listAmis:ArrayList<User>
    lateinit var currentUser_id:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_contacts, container, false)

        //get user connected
        val sharedPref= this.activity?.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()



        listAmis=ArrayList<User>()
        val recycler=view.findViewById<RecyclerView>(R.id.recycler_amis);
        recycler.layoutManager= LinearLayoutManager(this.context?.let { it}, RecyclerView.VERTICAL,false);


        RefreshRecycler(recycler, view.context,currentUser_id)

        val swipe=view.findViewById<SwipeRefreshLayout>(R.id.swipe_contact)
        swipe.setOnRefreshListener {
            RefreshRecycler(recycler, view.context, currentUser_id)
            if(swipe.isRefreshing){
                swipe.setRefreshing(false);
            }
        }

        return view
    }

    fun RefreshRecycler(recycler: RecyclerView, context: Context, currentUser_id: String) {
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getAllAmis(currentUser_id)
        retrofitData.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful){
                    listAmis=response.body()!!
                    recycler.adapter= AmisAdapter(listAmis,context)
                    recycler.scrollToPosition(0);
                }else{
                    Log.e("response invi",response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e("onFailure_Amis",t.toString())
            }
        })
    }
}