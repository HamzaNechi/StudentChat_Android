package com.example.studentchat.adapters

import android.content.Context
import android.content.Intent
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
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.activities.Chat
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AmisAdapter(val listAmis:ArrayList<User>, val ctx: Context): RecyclerView.Adapter<AmisAdapter.ViewHolder>()  {

    lateinit var currentUser_id:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_amis,parent,false);


        return AmisAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get user connected
        val sharedPref= ctx.getSharedPreferences("userConnected",Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()

        val data:User=listAmis[position];

        Glide.with(holder.itemView)
            .load(data.image)
            .into(holder.img_user)

        holder.name_user.text=data.username

        if(data.status.equals("En ligne")){
            holder.status.visibility=View.VISIBLE
        }else{
            holder.status.visibility=View.GONE
        }

        holder.itemView.setOnClickListener {
            val map=HashMap<String,String>();
            map.put("currentUser",currentUser_id)
            map.put("chatUser",data.id)
            val retrofitBuilder=ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.getOrCreateRoomPrive(map)
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful){
                        val chat_id= response.body()!!
                        val i=Intent(ctx, Chat::class.java)
                        i.putExtra("chatUser_name",data.username);
                        i.putExtra("chatUser_image",data.image);
                        i.putExtra("chat_id",chat_id);
                        ctx.startActivity(i)
                    }else{
                        Log.e("faile get chat_id",response.body().toString())
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("onFailure_chat_id",t.toString())
                }
            })

        }


        holder.delete.setOnClickListener {
            deleteAmis(data.id,currentUser_id,position)
        }
    }



    override fun getItemCount(): Int {
        return listAmis.size;
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val img_user=view.findViewById<ImageFilterView>(R.id.img_user_amis);
        val name_user=view.findViewById<TextView>(R.id.name_user_amis);
        val delete=view.findViewById<Button>(R.id.delete_amis)
        val status=view.findViewById<TextView>(R.id.status_connect)
    }

    private fun deleteAmis(id: String, currentUser_id: String, position: Int) {
        val map=HashMap<String,String>()
        map.put("userDelete",id);
        map.put("currentUser",currentUser_id);
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.deleteAmis(map)
        retrofitData.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful){
                    listAmis.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("onFailure_DeleteAmis",t.toString())
            }
        })
    }
}