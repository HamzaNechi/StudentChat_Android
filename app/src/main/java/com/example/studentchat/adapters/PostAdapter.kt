package com.example.studentchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.R
import com.example.studentchat.entity.Post
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(val listPost:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val name_user=view.findViewById<TextView>(R.id.name_user);
        val date=view.findViewById<TextView>(R.id.date)
        val img=view.findViewById<CircleImageView>(R.id.img_user);
        val desc=view.findViewById<TextView>(R.id.content_post)
        val content=view.findViewById<ImageView>(R.id.img_post);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false);
        return PostAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Post=listPost[position];
        holder.img.setImageResource(data.u.img);
        holder.name_user.text=data.u.nom;
        holder.date.text=data.date;
        holder.desc.text=data.description;
        holder.content.setImageResource(data.image);
    }

    override fun getItemCount(): Int {
        return listPost.size;
    }
}