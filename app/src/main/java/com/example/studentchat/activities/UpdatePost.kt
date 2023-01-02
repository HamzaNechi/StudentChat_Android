package com.example.studentchat.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.RealPathUtil
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class UpdatePost() : AppCompatActivity() {
    lateinit var img:ImageView;
    private var path:String="";
    lateinit var photolink:String;
    lateinit var currentUser_id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)


        val sharedPref= this.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()

        val content=findViewById<TextView>(R.id.content_update_post)
        img=findViewById<ImageView>(R.id.img_post_update)
        val pickGallery=findViewById<Button>(R.id.pick_gallery)
        val pickCamer=findViewById<Button>(R.id.pick_camera)
        val update=findViewById<Button>(R.id.updatepost)
        val annuler=findViewById<Button>(R.id.dismiss_update)

        val user= User(id = intent.getStringExtra("user_id").toString(),username="a", password = "a", role = "a", email = "a", image = "a", status = "a")
        val post:Post= Post(id = intent.getStringExtra("_id").toString(), image = intent.getStringExtra("image").toString(), author = intent.getStringExtra("author").toString(),
            date = Date(), description = intent.getStringExtra("content").toString(), u = user
        )
        photolink=post.image;
        if(!post.image.isNullOrEmpty() && post.image != "empty"){
            Glide.with(this)
                .load(post.image)
                .into(img)
            content.text=post.description
            img.visibility= View.VISIBLE
        }else{
            content.text=post.description
        }
        pickGallery.setOnClickListener {
            val iG= Intent(Intent.ACTION_PICK)
            iG.data= MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(iG,1);
        }

        pickCamer.setOnClickListener {
            val iC= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(iC,2);
        }


        update.setOnClickListener {
            updatePost(post,content.text.toString())
        }




        annuler.setOnClickListener {
            this.finish()
        }
    }

    private fun updatePost(post: Post, NewDescription: String) {
        //Retrofit
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val f: File
        val call: Call<ServerResponse>
        if(path.isNullOrEmpty()){
            /** update post sans image***/
            //add to database
            val map=HashMap<String,String>();
            map.put("description",NewDescription);
            map.put("id",post.id);
            call=retrofitBuilder.updatePostSansImage(map);
        }else{
            /**update post avec image**/
            //save post to database
            f= File(path);
            val reqFile: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)
            val body: MultipartBody.Part= MultipartBody.Part.createFormData("image",f.name,reqFile)
            val description: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), NewDescription)
            val post_id: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), post.id)
            call=retrofitBuilder.updatePost(body,description,post_id);
        }

        call.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@UpdatePost,"post modifier avec succé",Toast.LENGTH_LONG).show();
                    val i=Intent(this@UpdatePost,Home::class.java);
                    startActivity(i)
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("add posst error",t.toString())
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                img.visibility=View.VISIBLE
                img.setImageURI(data?.data)
                val rl= RealPathUtil()
                val p: String? = data?.data?.let { rl.getRealPath(this, it) }
                if(!p.isNullOrEmpty()){
                    path=p;
                }
                Log.e("Real paath image : ",path)
            }else{
                if(requestCode == 2){
                    val bitmap=data?.extras?.get("data") as Bitmap
                    img.visibility=View.VISIBLE
                    img.setImageBitmap(bitmap)
                    //bitmap to ur
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    var url: String? = MediaStore.Images.Media.insertImage(
                        this.getContentResolver(),
                        bitmap,
                        currentUser_id,
                        null
                    )

                    val rl= RealPathUtil()
                    val p: String? = rl.getRealPath(this, Uri.parse(url))
                    if(!p.isNullOrEmpty()){
                        path=p;
                    }
                }
            }
        }
    }
}