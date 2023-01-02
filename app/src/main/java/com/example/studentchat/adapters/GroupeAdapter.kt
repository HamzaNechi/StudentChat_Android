package com.example.studentchat.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.entity.Chat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupeAdapter(val listGroups:ArrayList<Chat>,val context: Context):RecyclerView.Adapter<GroupeAdapter.ViewHolder>(){


    lateinit var currentUser_id:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_groupe,parent,false);
        return GroupeAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=listGroups[position]
        Glide.with(holder.itemView).load(data.image).into(holder.image)
        holder.nom.text=data.nom

        val sharedPref=context.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()
        holder.btnQuit.setOnClickListener {
            //quitter le groupe
            val map=HashMap<String,String>();
            map.put("chat_id",data._id)
            map.put("user_id",currentUser_id)
            QuitterLeGroupe(map,position)
        }

        holder.itemView.setOnClickListener {
            //entrez dans la conversation
            val i= Intent(context,com.example.studentchat.activities.Chat::class.java)
            i.putExtra("chatUser_name",data.nom);
            i.putExtra("chatUser_image",data.image);
            i.putExtra("chat_id",data._id);
            context.startActivity(i);
        }
    }

    private fun QuitterLeGroupe(map: HashMap<String, String>, position: Int) {
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.quitterGroupe(map)
        retrofitData.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful){
                    listGroups.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("onFailure_Refuseinvi",t.toString())
            }
        })
    }

    override fun getItemCount(): Int {
        return listGroups.size;
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image=view.findViewById<androidx.constraintlayout.utils.widget.ImageFilterView>(R.id.img_groupe)
        val nom=view.findViewById<TextView>(R.id.name_groupe)
        val btnQuit=view.findViewById<Button>(R.id.quit_groupe)
    }

}


