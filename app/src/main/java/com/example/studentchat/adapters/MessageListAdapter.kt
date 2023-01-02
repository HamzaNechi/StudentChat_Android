package com.example.studentchat.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.R
import com.example.studentchat.entity.Chat
import com.example.studentchat.entity.ChatResponse
import com.example.studentchat.entity.message
import de.hdodenhof.circleimageview.CircleImageView

class MessageListAdapter(val listChat:ArrayList<ChatResponse>, val arrChat_id:ArrayList<String>):RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_messages,parent,false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=listChat[position]
        if(arrChat_id.contains(data.chat_id._id)){
            if(data.chat_id.nom.isNullOrEmpty()){
                holder.nom.text=data.user.username
                holder.msg.text=data.content;
                holder.time.text=data.date.time.toString();
                Glide.with(holder.itemView)
                    .load(data.user.image)
                    .into(holder.img)
            }else{
                holder.nom.text=data.chat_id.nom
                holder.msg.text=data.content;
                holder.time.text=data.date.time.toString();
                Glide.with(holder.itemView)
                    .load(data.chat_id.image)
                    .into(holder.img)
            }

        }

       holder.itemView.setOnClickListener {
           val i=Intent(it.context,com.example.studentchat.activities.Chat::class.java);
           if(data.chat_id.nom.isNullOrEmpty()){
               i.putExtra("chatUser_name",data.user.username)
               i.putExtra("chatUser_image",data.user.image)
               i.putExtra("chat_id",data.chat_id._id)
           }else{
               i.putExtra("chatUser_name",data.chat_id.nom)
               i.putExtra("chatUser_image",data.chat_id.image)
               i.putExtra("chat_id",data.chat_id._id)
           }
           it.context.startActivity(i);

       }

    }

    override fun getItemCount(): Int {
        return arrChat_id.size;
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val nom=view.findViewById<TextView>(R.id.name_user);
        val img=view.findViewById<CircleImageView>(R.id.img_user);
        val time=view.findViewById<TextView>(R.id.time);
        val msg=view.findViewById<TextView>(R.id.msg);
    }

}