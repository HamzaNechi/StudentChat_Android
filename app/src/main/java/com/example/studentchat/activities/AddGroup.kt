package com.example.studentchat.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.RealPathUtil
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.Interface.SocketHandler
import com.example.studentchat.R
import com.example.studentchat.adapters.SearchUserAdapter
import com.example.studentchat.entity.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddGroup : AppCompatActivity() {
    lateinit var listMembre:ArrayList<User>
    lateinit var pick_img: ImageFilterView
    private var path:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)


        val close=findViewById<ImageView>(R.id.close_add_groupe)
        close.setOnClickListener {
            this.finish()
        }

        pick_img=findViewById(R.id.img_grp_chat);
        val nom_grp=findViewById<TextInputEditText>(R.id.nomgrp);

        pick_img.setOnClickListener {
            PickImage()
        }

        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        val mSocket = SocketHandler.getSocket()

        val add_membre=findViewById<Button>(R.id.add_membre_button)
        val voir_membres=findViewById<Button>(R.id.voir_membre)
        val listMembreR=findViewById<RecyclerView>(R.id.recycler_membre_addgroupe)
        listMembreR.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false);




        val listSearch=findViewById<RecyclerView>(R.id.recycler_search_addgroupe)
        listSearch.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        listMembre=ArrayList<User>();
        //notification to add membre
        mSocket.on("receive_membre") { args ->
            if (args[0] != null) {
                val msgsock = args[0]
                if(msgsock != null){
                    val jsonMembre= JSONObject(msgsock.toString())
                    val user=User(jsonMembre.getString("_id"),jsonMembre.getString("username"),"0","0","0",jsonMembre.getString("image"),"0")
                    Log.i("receive socket",user.username)
                    listMembre.add(user)

                }
                /*runOnUiThread {
                    // The is where you execute the actions after you receive the data
                }*/
            }
        }


        //notification to delete membre
        mSocket.on("remove_membre") { args ->
            if (args[0] != null) {
                val msgsock = args[0]
                if(msgsock != null){
                    val jsonMembre= JSONObject(msgsock.toString())
                    val user=User(jsonMembre.getString("_id"),jsonMembre.getString("username"),"0","0","0",jsonMembre.getString("image"),"0")
                    Log.i("receive socket",user.username)
                    listMembre.remove(user);
                    MainScope().launch {
                        withContext(context = Dispatchers.Default) {
                            this
                        }
                        listMembreR.adapter?.notifyDataSetChanged()

                    }
                }
            }
        }





        add_membre.setOnClickListener {
            listMembreR.visibility= View.GONE
            listSearch.visibility=View.VISIBLE
        }

        voir_membres.setOnClickListener {
            listMembreR.adapter=SearchUserAdapter(listMembre,this,"display")
            listMembreR.visibility= View.VISIBLE
            listSearch.visibility=View.GONE
        }


        val search=findViewById<SearchView>(R.id.search_membre)
        search.clearFocus();


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextChange(qString: String): Boolean {
                val retrofitBuilder= ApiPostInterface.retrofitBuilder
                val map=HashMap<String,String>();
                map.put("name",qString);
                val retrofitData=retrofitBuilder.FetchUser(map)
                retrofitData.enqueue(object : Callback<ArrayList<User>> {
                    override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                        if (response.isSuccessful){
                            if(response.body() != null){
                                val listusers:ArrayList<User> = response.body()!!.toCollection(kotlin.collections.ArrayList())
                                listSearch.adapter= SearchUserAdapter(listusers,this@AddGroup,"fetch");
                                listSearch.adapter?.notifyDataSetChanged()
                            }else{
                                Log.e("response fetch","data null")
                            }

                        }

                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Log.e("onFailure",t.toString())
                    }
                })
                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
               // filtreUser(qString)
                return true
            }
        })

        //add groupe
        val sharedPref= this.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        val currentUser_id = sharedPref?.getString("_id","default value").toString()
        val btnadd=findViewById<Button>(R.id.btnAddGroupe)
        btnadd.setOnClickListener {
            val map=kotlin.collections.HashMap<String,String>();

            val f= File(path);
            val reqFile: RequestBody =RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)
            val image: MultipartBody.Part= MultipartBody.Part.createFormData("image",f.name+f.extension,reqFile)
            val admin: RequestBody =RequestBody.create("multipart/form-data".toMediaTypeOrNull(),currentUser_id)
            val nom: RequestBody =RequestBody.create("multipart/form-data".toMediaTypeOrNull(),nom_grp.text.toString())
            var arrUser= ArrayList<RequestBody>();
            arrUser.add(RequestBody.create("multipart/form-data".toMediaTypeOrNull(),currentUser_id))
            for (item in listMembre){
                arrUser.add(RequestBody.create("multipart/form-data".toMediaTypeOrNull(),item.id))
            }

            val retrofitBuilder= ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.addRoomChat(image,nom,admin,arrUser);
            retrofitData.enqueue(object : Callback<com.example.studentchat.entity.Chat> {
                override fun onResponse(call: Call<com.example.studentchat.entity.Chat>, response: Response<com.example.studentchat.entity.Chat>) {
                    if (response.isSuccessful){
                        val chat=response.body()!!
                        if (chat != null){
                            val i=Intent(this@AddGroup, Chat::class.java)
                            i.putExtra("chatUser_name",chat.nom);
                            i.putExtra("chatUser_image",chat.image);
                            i.putExtra("chat_id",chat._id);
                            this@AddGroup.startActivity(i)
                        }else{
                            //alert
                        }

                    }

                }

                override fun onFailure(call: Call<com.example.studentchat.entity.Chat>, t: Throwable) {
                    Log.e("onFailure",t.toString())
                }
            })
        }


    }

    private fun PickImage() {
        val iG= Intent(Intent.ACTION_PICK)
        iG.data= MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(iG,1);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                pick_img.setImageURI(data?.data)
                val rl= RealPathUtil()
                val p: String? = data?.data?.let { rl.getRealPath(this, it) }
                if(!p.isNullOrEmpty()){
                    path=p;
                }
                Log.e("Real paath image : ",path)
            }
        }
    }
}