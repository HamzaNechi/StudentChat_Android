package com.example.studentchat.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.entity.Invitations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitationAdapter(val listInv:ArrayList<Invitations>,val ctx:Context): RecyclerView.Adapter<InvitationAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_invitation,parent,false);
        return InvitationAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Invitations=listInv[position]
        Glide.with(holder.itemView)
            .load(data.expediteur.image)
            .into(holder.img_user)
        holder.name_user.text=data.expediteur.username
        holder.date.text="11/12/2022"


        //actions click

        holder.accept.setOnClickListener {
            acceptInvitation(data._id,position)
        }

        holder.refuse.setOnClickListener {
            refuseInvitation(data._id,position)
        }
    }




    override fun getItemCount(): Int {
       return listInv.size
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val img_user=view.findViewById<ImageFilterView>(R.id.img_user_inv);
        val name_user=view.findViewById<TextView>(R.id.name_user_inv);
        val date=view.findViewById<TextView>(R.id.date_inv);
        val accept=view.findViewById<Button>(R.id.accept_inv);
        val refuse=view.findViewById<Button>(R.id.refuse_inv);
    }


    private fun acceptInvitation(_id: String, position: Int) {

        val map=HashMap<String,String>()
        map.put("id",_id);
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.acceptInvitation(map)
        retrofitData.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful){
                    listInv.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("onFailure_Acceptinvi",t.toString())
            }
        })
    }


    private fun refuseInvitation(_id: String, position: Int) {
        val map=HashMap<String,String>()
        map.put("id",_id);
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.refuseInvitation(map)
        retrofitData.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful){
                    listInv.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("onFailure_Refuseinvi",t.toString())
            }
        })
    }
}