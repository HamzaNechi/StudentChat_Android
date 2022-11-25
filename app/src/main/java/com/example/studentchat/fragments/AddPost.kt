package com.example.studentchat.fragments


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.InputStream


class AddPost : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var pickImage: Button
    private lateinit var upload: Button
    lateinit var pd:ProgressDialog
    lateinit var part_image:String
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
        //Retrofit
        /**val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.12:9090/")
            .build()
            .create(ApiPostInterface::class.java)
        val imageFile=File(part_image)

        val requestBody:RequestBody=RequestBody.create("multipart/form-file".toMediaTypeOrNull(),imageFile)
        val partImage:MultipartBody.Part=MultipartBody.Part.createFormData("image",imageFile.name,requestBody)
       /* val retrofitdata=retrofitBuilder.addPost(partImage)
        retrofitdata.enqueue(object: Call<ServerResponse>{

        })*/
        */
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            if (requestCode === REQUEST_GALLERY) {
                data?.data?.let { returnUri ->
                    this.context?.contentResolver?.query(returnUri, null, null, null, null)
                }?.use { cursor ->
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    cursor.moveToFirst()
                    val indexImage = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    part_image = cursor.getString(indexImage)
                    Log.e("part image",part_image)
                    if (part_image != null) {
                        val yourFilePath:String= Environment.DIRECTORY_PICTURES.toString()
                        val image: File = File(yourFilePath,part_image)
                        Log.e("absolute path",image.absolutePath)
                        imageView.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))
                    }else{
                        Log.e("part image","part image null")
                    }
                }
            }
        }
    }


}