package com.example.studentchat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.SocketHandler
import com.example.studentchat.R
import com.example.studentchat.entity.User
import com.example.studentchat.entity.message
import org.json.JSONObject

class SearchUserAdapter(val listUser:ArrayList<User>,val ctx:Context,val type:String): RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(com.example.studentchat.R.layout.item_search_user_addgroupe,parent,false);
        return SearchUserAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //
        if(type.equals("fetch")){
            holder.btnDelete.visibility=View.GONE
            holder.btnAdd.visibility=View.VISIBLE
        }else{
            holder.btnDelete.visibility=View.VISIBLE
            holder.btnAdd.visibility=View.GONE
        }
        //get socket
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        val mSocket = SocketHandler.getSocket()

        val data=listUser[position]
        holder.name.text=data.username;
        Glide.with(holder.itemView).load(data.image).into(holder.img);

        //add to membre groupe by socket in AddGroup activity
        holder.btnAdd.setOnClickListener {
            val map=HashMap<String,String>()
            map.put("_id",data.id)
            map.put("username",data.username)
            map.put("role",data.role)
            map.put("image",data.image)
            map.put("password","0")
            map.put("email",data.email)
            map.put("status",data.status)
            val jsonData: JSONObject = JSONObject(map as Map<String, String>?)
            //send msg for all user ( socket ) (hné emit / node on)
            mSocket.emit("send_membre", jsonData)
            Toast.makeText(it.context,data.username+" ajouté au groupe",Toast.LENGTH_LONG).show();
        }

        //delete membre from listMembre in AddGroup Activity by socket
        holder.btnDelete.setOnClickListener {
            val map=HashMap<String,String>()
            map.put("_id",data.id)
            map.put("username",data.username)
            map.put("role",data.role)
            map.put("image",data.image)
            map.put("password","0")
            map.put("email",data.email)
            map.put("status",data.status)
            val jsonData: JSONObject = JSONObject(map as Map<String, String>?)
            //send msg for all user ( socket ) (hné emit / node on)
            mSocket.emit("delete_membre", jsonData)
            Toast.makeText(it.context,data.username+" supprimé du groupe",Toast.LENGTH_LONG).show();
        }
    }

    override fun getItemCount(): Int {
        return listUser.size;
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view)  {
        val img=view.findViewById<ImageFilterView>(R.id.img_user_newgroupe)
        val name=view.findViewById<TextView>(R.id.name_user_newgroupe)
        val btnAdd=view.findViewById<Button>(R.id.add_membre);
        val btnDelete=view.findViewById<Button>(R.id.delete_membre)
    }


}