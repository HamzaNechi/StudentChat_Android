package com.example.studentchat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.R
import com.example.studentchat.entity.message
import kr.co.prnd.readmore.ReadMoreTextView


class ChatAdapter(val listMsg:ArrayList<message>,val ctx:Context): RecyclerView.Adapter<ChatAdapter.ViewHolder>()  {
    lateinit var currentUser_id:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(com.example.studentchat.R.layout.item_message_send,parent,false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get user connected
        val sharedPref= ctx.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()
        val data=listMsg[position]
        if(data.user.id.equals(currentUser_id)){
            holder.send_layout.visibility=View.VISIBLE
            holder.receive_layout.visibility=View.GONE
            holder.msg.text=data.content
        }else{
            holder.send_layout.visibility=View.GONE
            holder.receive_layout.visibility=View.VISIBLE
            holder.msg_rec.text=data.content
            Glide.with(holder.itemView).load(data.user.image)
                .into(holder.img)
        }
    }

    override fun getItemCount(): Int {
        return listMsg.size
    }

    open class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val img=view.findViewById<ImageView>(R.id.img_user_receive)
        val msg_rec=view.findViewById<ReadMoreTextView>(R.id.msg_receive)
        val msg=view.findViewById<ReadMoreTextView>(R.id.msg_send)
        val send_layout=view.findViewById<RelativeLayout>(R.id.item_send)
        val receive_layout=view.findViewById<RelativeLayout>(R.id.item_receive)
    }
}