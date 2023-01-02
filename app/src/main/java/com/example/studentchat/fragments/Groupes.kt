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
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.R
import com.example.studentchat.adapters.AmisAdapter
import com.example.studentchat.adapters.GroupeAdapter
import com.example.studentchat.entity.Chat
import com.example.studentchat.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Groupes(val ctx:Context) : Fragment() {
    lateinit var listGroups:ArrayList<Chat>
    lateinit var currentUser_id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_groupes, container, false)
        val swipe=view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe_groupe)
        val recycler=view.findViewById<RecyclerView>(R.id.recycler_groupe)
        recycler.layoutManager= LinearLayoutManager(this.context?.let { it}, RecyclerView.VERTICAL,false);

        //get user connected
        val sharedPref= this.activity?.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString();

        RefreshRecycler(recycler, view.context,currentUser_id)

        //swipe refresh
        swipe.setOnRefreshListener {
            RefreshRecycler(recycler, view.context, currentUser_id)
            if(swipe.isRefreshing){
                swipe.setRefreshing(false);
            }
        }
        return view
    }

    fun RefreshRecycler(recycler: RecyclerView, context: Context, currentUser_id: String) {
        val map=HashMap<String,String>();
        map.put("user_id",currentUser_id)
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getAllGroupe(map)
        retrofitData.enqueue(object : Callback<ArrayList<Chat>> {
            override fun onResponse(call: Call<ArrayList<Chat>>, response: Response<ArrayList<Chat>>) {
                if (response.isSuccessful){
                    listGroups=response.body()!!
                    recycler.adapter= GroupeAdapter(listGroups,context)
                    recycler.scrollToPosition(0);
                }else{
                    Log.e("response groupe",response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<Chat>>, t: Throwable) {
                Log.e("onFailure_groupe",t.toString())
            }
        })
    }
}