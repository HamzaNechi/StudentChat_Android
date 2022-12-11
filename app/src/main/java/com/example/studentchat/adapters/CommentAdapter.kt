package com.example.studentchat.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.entity.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(val listComment:ArrayList<Comment>, val ctx:Context): RecyclerView.Adapter<CommentAdapter.ViewHolder>()  {

    lateinit var currentUser_id:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.comment_post_item,parent,false);
        return CommentAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Comment=listComment[position]
        val sharedPref=ctx.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()
        //load image user
        Glide.with(holder.itemView)
            .load(data.user.image)
            .into(holder.img)
        holder.name.text=data.user.username
        holder.content.text=data.content



        //accé de supprimer commentaire si l'utilisateur connecté howa bidou moula lcommentaire
        if(data.user.id.equals(currentUser_id)){
            holder.content.setOnLongClickListener{
                val delete= arrayOf("Supprimer votre commentaire")
                val pos:Int=position;
                val alert:AlertDialog.Builder=AlertDialog.Builder(it.context)
                alert.setItems(delete,DialogInterface.OnClickListener { dialogInterface, i ->
                    if(i == 0){
                        //delete comment
                        val retrofitBuilder= ApiPostInterface.retrofitBuilder
                        val retrofitData=retrofitBuilder.deleteComment(data._id)
                        retrofitData.enqueue(object : Callback<ServerResponse> {
                            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                                if (response.isSuccessful){
                                    listComment.removeAt(pos)
                                    notifyItemRemoved(pos)
                                }

                            }

                            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                                Log.e("onFailure DeleteComment",t.toString())
                            }
                        })
                    }
                })
                alert.create().show()
               // Toast.makeText(it.context,"Delete item ok",Toast.LENGTH_LONG).show()
                return@setOnLongClickListener true
            }
        }

    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img=view.findViewById<ImageView>(R.id.img_user_comment)
        val name=view.findViewById<TextView>(R.id.name_user_comment)
        val content=view.findViewById<TextView>(R.id.content_comment)
    }
}