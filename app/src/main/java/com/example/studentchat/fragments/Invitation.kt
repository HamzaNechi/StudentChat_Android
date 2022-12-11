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
import com.example.studentchat.adapters.InvitationAdapter
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Invitations
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class Invitation(val ctx:Context) : Fragment() {

    lateinit var listInvitation:ArrayList<Invitations>
    lateinit var currentUser_id:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get user connected
        val sharedPref= this.activity?.getSharedPreferences("userConnected",Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()


        // Inflate the layout for this fragment
        listInvitation=ArrayList<Invitations>()
        val view:View=inflater.inflate(R.layout.fragment_invitation, container, false)
        val recycler=view.findViewById<RecyclerView>(R.id.recycler_inv);
        recycler.layoutManager= LinearLayoutManager(this.context?.let { it}, RecyclerView.VERTICAL,false);
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getInvitationEnAttente(currentUser_id)
        retrofitData.enqueue(object : Callback<ArrayList<Invitations>> {
            override fun onResponse(call: Call<ArrayList<Invitations>>, response: Response<ArrayList<Invitations>>) {
                if (response.isSuccessful){
                    listInvitation=response.body()!!
                    for (item in listInvitation){
                        Log.e("item invi",item._id)
                    }

                    recycler.adapter= InvitationAdapter(listInvitation,view.context)
                    recycler.scrollToPosition(0);
                }else{
                    Log.e("response invi",response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<Invitations>>, t: Throwable) {
                Log.e("onFailure_Invitations",t.toString())
            }
        })
        val swipe=view.findViewById<SwipeRefreshLayout>(R.id.swipe_inv)
        swipe.setOnRefreshListener {
            RefreshRecycler(recycler, view.context)
            if(swipe.isRefreshing){
                swipe.setRefreshing(false);
            }
        }
        return view;
    }

    private fun RefreshRecycler(recycler: RecyclerView, context: Context) {
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getInvitationEnAttente(currentUser_id)
        retrofitData.enqueue(object : Callback<ArrayList<Invitations>> {
            override fun onResponse(call: Call<ArrayList<Invitations>>, response: Response<ArrayList<Invitations>>) {
                if (response.isSuccessful){
                    listInvitation=response.body()!!
                  //  val listPosts:ArrayList<Post> = lp.posts.toCollection(kotlin.collections.ArrayList())
                    recycler.adapter= InvitationAdapter(listInvitation,context)
                    recycler.scrollToPosition(0);
                }

            }

            override fun onFailure(call: Call<ArrayList<Invitations>>, t: Throwable) {
                Log.e("onFailure_Invitations",t.toString())
            }
        })
    }
}