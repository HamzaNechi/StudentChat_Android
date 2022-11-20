package com.example.studentchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.R
import com.example.studentchat.entity.Chat
import com.example.studentchat.entity.User
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(val listChat:ArrayList<Chat>):RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Chat=listChat[position]
        holder.nom.text=data.nom
        holder.msg.text=data.msg;
        holder.time.text=data.time;
        holder.img.setImageResource(data.img);

    }

    override fun getItemCount(): Int {
        return listChat.size;
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val nom=view.findViewById<TextView>(R.id.name_user);
        val img=view.findViewById<CircleImageView>(R.id.img_user);
        val time=view.findViewById<TextView>(R.id.time);
        val msg=view.findViewById<TextView>(R.id.msg);
    }

}