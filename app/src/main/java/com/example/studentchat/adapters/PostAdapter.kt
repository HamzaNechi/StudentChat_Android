package com.example.studentchat.adapters

import android.icu.text.DateIntervalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.R
import com.example.studentchat.entity.Post
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class PostAdapter(val listPost:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false);
        return PostAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Post=listPost[position];
        //load image user
        Glide.with(holder.itemView)
            .load(data.u.image)
            .into(holder.img)
       holder.name_user.text=data.u.username;

        //Convert date format
        val sdf=SimpleDateFormat("dd/MM/yyyy")
        holder.date.text=sdf.format(data.date)
        //endConvert date

        //if text empty
        if(data.description.isNullOrEmpty()){
            holder.desc.visibility=View.GONE;
        }else{
            holder.desc.text=data.description;
        }

        if (data.image.isNullOrEmpty() || data.image.equals("empty")){
            holder.img_post.visibility=View.GONE;
        }else{
            holder.img_post.visibility=View.VISIBLE;
            //load image post
            Glide.with(holder.itemView)
                .load(data.image)
                .into(holder.img_post)
        }
    }

    override fun getItemCount(): Int {
        return listPost.size;
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val name_user=view.findViewById<TextView>(R.id.name_user);
        val date=view.findViewById<TextView>(R.id.date)
        val img=view.findViewById<ImageFilterView>(R.id.img_user);
        val desc=view.findViewById<kr.co.prnd.readmore.ReadMoreTextView>(R.id.desc)
        val img_post=view.findViewById<ImageView>(R.id.img_post);
    }
}