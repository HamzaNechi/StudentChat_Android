package com.example.studentchat.fragments



import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


class AddPost(val ctx: Context): Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var pickImage: Button
    private lateinit var upload: Button
    lateinit var pd:ProgressDialog
    lateinit var part_image:String
    lateinit var path:String
    final val REQUEST_GALLERY:Int=9457;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_add_post, container, false)

        imageView = view.findViewById(R.id.preview) as ImageView
        pickImage = view.findViewById(R.id.pickImage) as Button
        upload = view.findViewById(R.id.upload) as Button

        pd= ProgressDialog(this.context)
        pd.setMessage("Loading ...")



        pickImage.setOnClickListener {
            val i=Intent();
            i.type="image/*";
            i.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i,"open gallery"),REQUEST_GALLERY)
        }

        upload.setOnClickListener {
            addpost()
        }


        return view;
    }

    private fun addpost() {

        //add post local
        val u=User(id="6382818daab9fab49033f9b0", username = "Hamza nechi", password = "1234", role = "admin","nechi@ggg.com","http://192.168.1.11:9090/images/user/moi1669497229681.jpg")
        path="not found pour le moment"
        val entity_post=Post(id="eee",path,"add post from retrofit",Date(),u)
        //Retrofit
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.11:9090/")
            .build()
            .create(ApiPostInterface::class.java)

       // val f:File=File(path);
       // val reqFile:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)

       // val body:MultipartBody.Part=MultipartBody.Part.createFormData("image",f.name,reqFile)


        val description:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.description)
        val user:RequestBody=RequestBody.create("multipart/form-data".toMediaTypeOrNull(), entity_post.u.id)

       // val call:Call<ServerResponse> =retrofitBuilder.addPost(body,description,user);

        val call:Call<ServerResponse> =retrofitBuilder.addPost(description,user);

        call.enqueue(object :Callback<ServerResponse>{
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                Toast.makeText(ctx,"Chouf fel mongo sa3",Toast.LENGTH_LONG).show();
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("add posst error",t.toString())
            }

        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            if (requestCode === REQUEST_GALLERY) {
                data?.data?.let { returnUri ->
                    this.context?.contentResolver?.query(returnUri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                }?.use { cursor ->
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    /**fff*/
                    cursor!!.moveToNext()
                    val fileName = cursor.getString(0)
                    path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    if (!TextUtils.isEmpty(path)) {
                        Log.e("path 5000",path)
                        val ph:File=File(path)
                        if (ph.exists()) {
                            val myBitmap = BitmapFactory.decodeFile(ph.absolutePath)
                            val myImage = imageView
                            myImage.setImageBitmap(myBitmap)
                        }else{
                            Log.e("File not exist","path 4aaalit")
                        }
                       // imageView.setImageBitmap(BitmapFactory.decodeFile(path))
                    }
                   /** cursor.moveToFirst()
                    val indexImage = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    part_image = cursor.getString(indexImage)
                    Log.e("part image",part_image)
                    if (part_image != null) {
                        val yourFilePath:String=Environment.DIRECTORY_PICTURES.toString()
                        val image= File(yourFilePath,part_image)
                        imageView.setImageBitmap(BitmapFactory.decodeFile(Environment.DIRECTORY_PICTURES.toString()+image.name))
                    }else{
                        Log.e("part image","part image null")
                    }*/
                }
            }
        }
    }


}