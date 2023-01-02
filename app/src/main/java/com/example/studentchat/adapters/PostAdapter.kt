package com.example.studentchat.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.MenuRes
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.activities.Home
import com.example.studentchat.activities.UpdatePost
import com.example.studentchat.entity.Comment
import com.example.studentchat.entity.Like
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import com.example.studentchat.fragments.AddPost
import com.example.studentchat.fragments.DiscussionFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class PostAdapter(val listPost:ArrayList<Post>,val ctx: Context):RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    lateinit var currentUser_id:String
    lateinit var likes:ArrayList<Like>
    lateinit var listComment:ArrayList<Comment>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false);
        return PostAdapter.ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Post=listPost[position];

        val sharedPref=ctx.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()
        /********* si le post est partagé affiché le vrai auteur**********/
        if(!data.author.equals(data.u.id)){
            //get author user retrofit
            val retrofitBuilder=ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.getAuthor(data.author)
            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful){
                        holder.author.text=response.body()!!.username
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("onFailure getAuthor",t.toString())
                }
            })
            holder.txtauthor.visibility=View.VISIBLE;
            holder.author.visibility=View.VISIBLE;
        }
        //si user connecté déjà auteur afficher la liste des actions
        if(data.u.id.equals(currentUser_id)){
            holder.action.visibility=View.VISIBLE
            holder.action.setOnClickListener{
                showMenu(it, R.menu.action_menu_post,data)
            }
        }
        /********************************* end get author from post***************************/
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
        }
        else{
            holder.img_post.visibility=View.VISIBLE;
            //load image post
            Glide.with(holder.itemView)
                .load(data.image)
                .into(holder.img_post)
        }
        //si le currentUser aime le post on affiche le coeur black

        /************* get all likes***********************************/
        val retrofitBuilder=ApiPostInterface.retrofitBuilder

        val retrofitData=retrofitBuilder.AskisLike(data.id)
        retrofitData.enqueue(object : Callback<ArrayList<Like>> {
            override fun onResponse(call: Call<ArrayList<Like>>, response: Response<ArrayList<Like>>) {
                if (response.isSuccessful){
                    likes =response.body()!!.toCollection(kotlin.collections.ArrayList())
                    //val currentUser:String="63879ff48c6dbd9b06cfd89d"//Heni nechi
                    val like=Like(data.id,currentUser_id)

                    if (likes.isEmpty()){
                        Log.e("isLike",likes.toString())
                        holder.icon_like.setImageResource(R.drawable.ic_aime)
                        holder.likeCounter.text="0"
                        holder.txtisLike.text="addLike"
                    }else{
                        holder.likeCounter.text=likes.size.toString()
                        if(likes.contains(like)){
                            holder.icon_like.setImageResource(R.drawable.ic_aime_black)
                            holder.txtisLike.text="deleteLike"
                        }else{
                            holder.icon_like.setImageResource(R.drawable.ic_aime)
                            holder.txtisLike.text="addLike"
                        }
                    }

                    //onClick Like post
                    Log.e("Lbarra mn onClick ",likes.toString())
                    holder.onLike.setOnClickListener{
                            if(holder.txtisLike.text =="deleteLike" ){
                                var nbLike=holder.likeCounter.text.toString().toInt();
                                if(nbLike > 0){
                                nbLike -= 1;
                                }
                                holder.likeCounter.text=nbLike.toString();
                                holder.icon_like.setImageResource(R.drawable.ic_aime)
                                holder.txtisLike.text="addLike"
                                deleteLike(data.id,currentUser_id)
                               // Toast.makeText(it.context,"yemchi delete like",Toast.LENGTH_LONG).show()
                            }else{
                                var nbLike=holder.likeCounter.text.toString().toInt();
                                nbLike += 1;
                                holder.likeCounter.text=nbLike.toString();
                                holder.icon_like.setImageResource(R.drawable.ic_aime_black)
                                addLike(data.id,currentUser_id)
                                holder.txtisLike.text="deleteLike"
                                //Toast.makeText(it.context,like.post,Toast.LENGTH_LONG).show()
                            }


                    }
                    //end onClick like post
                }
            }

            override fun onFailure(call: Call<ArrayList<Like>>, t: Throwable) {
                Log.e("onFailure",t.toString())
            }
        })
        /************************* End get all likes ******************************/




        //onClick comment post
        holder.comment.setOnClickListener{
            val mDialog:Dialog=Dialog(it.context,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            //Toast.makeText(it.context,"this button is comment post",Toast.LENGTH_LONG).show()
            mDialog.setContentView(R.layout.comment_post_layout)
            mDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            val recycler=mDialog.findViewById<RecyclerView>(R.id.recycler_comment)
            recycler.layoutManager= LinearLayoutManager(it.context, RecyclerView.VERTICAL,false);
            mDialog.setTitle("Commentaires")
            /******Retrofit consommation comments getAllComment**********/
            val retrofitBuilder=ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.getComments(data.id)
            retrofitData.enqueue(object : Callback<ArrayList<Comment>> {
                override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                    if (response.isSuccessful){
                        listComment= response.body()!!.toCollection(kotlin.collections.ArrayList())
                        recycler.adapter=CommentAdapter(listComment,ctx);
                        holder.nbComment.text=listComment.size.toString()+" comments"
                    }

                }

                override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                    Log.e("onFailure getComments",t.toString())
                }
            })
            /******End get comments**************************************/


            mDialog.show()
            val cancel=mDialog.findViewById<RelativeLayout>(R.id.cancel_comment)
            val onComment=mDialog.findViewById<RelativeLayout>(R.id.comment_post_dialog)
            val content_post=mDialog.findViewById<TextInputEditText>(R.id.comment)
            val swipe=mDialog.findViewById<SwipeRefreshLayout>(R.id.swipe)
            onComment.setOnClickListener {
                val content_post=content_post.text.toString()
                val user=currentUser_id;
                val post=data.id;
                /************** add comment**************/
                val retrofitBuilder=ApiPostInterface.retrofitBuilder
                val map=HashMap<String,String>();
                map.put("post",post)
                map.put("user",user)
                map.put("content",content_post)
                val retrofitData=retrofitBuilder.addComment(map)
                retrofitData.enqueue(object : Callback<ServerResponse> {
                    override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                        if (response.isSuccessful){
                            RefreshRecyclerComment(mDialog.findViewById(R.id.recycler_comment),data.id)
                            mDialog.findViewById<TextInputEditText>(R.id.comment).text!!.clear();
                        }
                    }
                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        Log.e("onFailure share post",t.toString())
                    }
                })
                /**************end add comment*********/
            }

            swipe.setOnRefreshListener {
                mDialog.dismiss()
            }
            cancel.setOnClickListener {
                mDialog.dismiss()
            }
        }
        //end onClick comment post

        /************ onClick share post******************************/
        //onClick Partage post
        holder.partage.setOnClickListener{
            //sharePost(data.id,currentUser)
            val retrofitBuilder=ApiPostInterface.retrofitBuilder
            val map=HashMap<String,String>();
            map.put("post",data.id)
            map.put("user",currentUser_id)
            val retrofitData=retrofitBuilder.sharePost(map)
            retrofitData.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    if (response.isSuccessful){
                        //Refresh view
                        val i=Intent(it.context,Home::class.java)
                        it.context.startActivity(i)
                    }
                }
                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Log.e("onFailure share post",t.toString())
                }
            })
        }
        //end onClick Partage post
        /*************end onClick share post*************************/
    }




    override fun getItemCount(): Int {
        return listPost.size;
    }


    /*********************************** Like function*************************/
    private fun deleteLike(id: String, currentUser: String) {
        val retrofitBuilder=ApiPostInterface.retrofitBuilder
        val like=Like(id,currentUser)
        val retrofitData=retrofitBuilder.deleteLike(like)
        retrofitData.enqueue(object : Callback<ServerResponse>{
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                if(response.code() == 202){
                    Log.e("delete like","like deleted")
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("Failure delete like",t.toString())
            }

        })
    }

    private fun addLike(id: String, currentUser: String) {
        val retrofitBuilder=ApiPostInterface.retrofitBuilder
        val like=Like(id,currentUser)
        val retrofitData=retrofitBuilder.addLike(like)
        retrofitData.enqueue(object : Callback<ServerResponse>{
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                if(response.code() == 201){
                    Log.e("addLike","like added")
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("Failure add like",t.toString())
            }

        })

    }
    /***********************************End Like function********************/


    /******************************* show menu action post********************/
    //In the showMenu function from the previous example:
    @SuppressLint("RestrictedApi")
    private fun showMenu(v: View, @MenuRes menuRes: Int, post: Post) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.delete) {
                //delete post
                val retrofitBuilder= ApiPostInterface.retrofitBuilder
                val retrofitData=retrofitBuilder.deletePost(post.id)
                retrofitData.enqueue(object : Callback<ServerResponse> {
                    override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                        if (response.isSuccessful){
                            Toast.makeText(v.context,"post deleted",Toast.LENGTH_LONG).show()
                            val pos=listPost.indexOf(post)
                            listPost.removeAt(pos);
                            notifyItemRemoved(pos)
                        }

                    }

                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        Log.e("onFailure DeleteComment",t.toString())
                    }
                })
            } else if (item.itemId == R.id.edit) {
                val i=Intent(ctx,UpdatePost::class.java)
                i.putExtra("_id",post.id)
                i.putExtra("image",post.image)
                i.putExtra("author",post.author)
                i.putExtra("date",post.date)
                i.putExtra("user_id",currentUser_id)
                i.putExtra("content",post.description)
                ctx.startActivity(i);
            }
            true
        }
        popup.show()
    }

    /***************************** end show menu action post ******************/

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val name_user=view.findViewById<TextView>(R.id.name_user);
        val date=view.findViewById<TextView>(R.id.date)
        val img=view.findViewById<ImageFilterView>(R.id.img_user);
        val desc=view.findViewById<kr.co.prnd.readmore.ReadMoreTextView>(R.id.desc)
        val img_post=view.findViewById<ImageView>(R.id.img_post);
        val txtauthor=view.findViewById<TextView>(R.id.txtauthor);
        val author=view.findViewById<TextView>(R.id.author_name);
        val action=view.findViewById<ImageView>(R.id.more_action)
        /*************** Footer post**************************/
        val onLike=view.findViewById<RelativeLayout>(R.id.like_post);
        val likeCounter=view.findViewById<TextView>(R.id.like_counter);
        val icon_like=view.findViewById<ImageView>(R.id.icon_unlike);
        val txtisLike=view.findViewById<TextView>(R.id.isLike)
        val comment=view.findViewById<RelativeLayout>(R.id.comment_post);
        val nbComment=view.findViewById<TextView>(R.id.nb_comments);
        val partage=view.findViewById<RelativeLayout>(R.id.send_post)

    }

    /******** Refresh lists*******/
    private fun RefreshRecyclerComment(recycler: RecyclerView?, id_post:String) {
        Log.e("id_postRefresh recycler",id_post)
        val retrofitBuilder=ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getComments(id_post)
        retrofitData.enqueue(object : Callback<ArrayList<Comment>> {
            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                if (response.isSuccessful){
                    listComment= response.body()!!.toCollection(kotlin.collections.ArrayList())
                    recycler!!.adapter=CommentAdapter(listComment,ctx);
                    recycler!!.scrollToPosition(listComment.size-1)
                }

            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                Log.e("onFailure getComments",t.toString())
            }
        })

    }

}