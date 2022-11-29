package com.example.studentchat.fragments



import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.RealPathUtil
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.activities.Home
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.jar.Manifest


class AddPost(val ctx: Context): Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var description: TextInputEditText
    private lateinit var pickGallery: Button
    private lateinit var pickCamera: Button
    private lateinit var addpost: Button
    private var path:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_add_post, container, false)

        imageView = view.findViewById(R.id.img_post) as ImageView
        description=view.findViewById(R.id.desc_addpost) as TextInputEditText
        pickGallery = view.findViewById(R.id.pick_gallery) as Button
        pickCamera = view.findViewById(R.id.pick_camera) as Button
        addpost = view.findViewById(R.id.addpost) as Button


        pickGallery.setOnClickListener {
            val iG=Intent(Intent.ACTION_PICK)
            iG.data=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(iG,1);
        }

        pickCamera.setOnClickListener {
            val iC=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(iC,1);
        }

        addpost.setOnClickListener {
            addpost(description.text.toString())
        }


        return view;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                imageView.visibility=View.VISIBLE
                imageView.setImageURI(data?.data)
               val rl=RealPathUtil()
                val p: String? = data?.data?.let { rl.getRealPath(ctx, it) }
                if(!p.isNullOrEmpty()){
                    path=p;
                }
                Log.e("Real paath image : ",path)
            }
        }
    }

    private fun addpost(description:String) {


            //Retrofit
            val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.11:9090/")
            .build()
            .create(ApiPostInterface::class.java)



            val f:File
            val call:Call<ServerResponse>
            if(path.isNullOrEmpty()){
            /** add post sans image***/
            path="empty"
            //add post local
            val u=User(id="6382818daab9fab49033f9b0", username = "Hamza nechi", password = "1234", role = "admin","nechi@ggg.com","http://172.16.11.19:9090/images/user/moi1669497229681.jpg")
            val entity_post=Post(id="eee",path,description,Date(),u)
            //add to database
            val description:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.description)
            val user:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.u.id)
            call=retrofitBuilder.addPostSansImage(description,user);
        }else{
            /**add post avec image**/
            //add post local
            val u=User(id="6382818daab9fab49033f9b0", username = "Hamza nechi", password = "1234", role = "admin","nechi@ggg.com","http://172.16.11.19:9090/images/user/moi1669497229681.jpg")
            val entity_post=Post(id="eee",path,description,Date(),u)
            //save post to database
            f=File(path);
            val reqFile:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)
            val body: MultipartBody.Part=MultipartBody.Part.createFormData("image",f.name,reqFile)
            val description:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.description)
            val user:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.u.id)
            call=retrofitBuilder.addPost(body,description,user);
        }

        call.enqueue(object :Callback<ServerResponse>{
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                Toast.makeText(ctx,"Votre article à été ajouté avec succé",Toast.LENGTH_LONG).show();
                val i=Intent(ctx,Home::class.java)
                startActivity(i)
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("add posst error",t.toString())
            }

        })


    }


}