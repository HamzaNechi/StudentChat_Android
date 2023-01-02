package com.example.studentchat.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.Interface.SocketHandler
import com.example.studentchat.R
import com.example.studentchat.adapters.ChatAdapter
import com.example.studentchat.entity.User
import com.example.studentchat.entity.message
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import io.socket.client.IO;
import io.socket.client.Socket;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.collections.HashMap


class Chat : AppCompatActivity() {
    lateinit var currentUser_id:String
    lateinit var listMsg:ArrayList<message>;
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        val mSocket = SocketHandler.getSocket()

        //get user connected
        val sharedPref= this.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()
        val currentUser_username = sharedPref?.getString("username","default value").toString()
        val currentUser_image = sharedPref?.getString("image","default value").toString()



        val image_user=findViewById<ImageFilterView>(R.id.img_user_chat)
        val url_image=intent.getStringExtra("chatUser_image")
        Glide.with(this).load(url_image).into(image_user)


        val name_user=findViewById<TextView>(R.id.name_user_chat)
        name_user.text=intent.getStringExtra("chatUser_name")

        val close=findViewById<ImageView>(R.id.close_chat)
        val recycler_msgs=findViewById<RecyclerView>(R.id.messenger)
        recycler_msgs.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        val chat_id=intent.getStringExtra("chat_id")
        //val listMsg=ArrayList<message>();
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.getMsgParChat(chat_id.toString())
        retrofitData.enqueue(object : Callback<ArrayList<message>> {
            override fun onResponse(call: Call<ArrayList<message>>, response: Response<ArrayList<message>>) {
                if (response.isSuccessful){
                    listMsg=response.body()!!
                    recycler_msgs.adapter=ChatAdapter(listMsg,this@Chat)
                    recycler_msgs.scrollToPosition(listMsg.size)
                }else{
                    Log.e("fail response",response.body().toString())
                }

            }

            override fun onFailure(call: Call<ArrayList<message>>, t: Throwable) {
                Log.e("onFailure_msg",t.toString())
            }
        })


        //send message
        val content=findViewById<TextInputEditText>(R.id.send_msg)
        val send=findViewById<ImageButton>(R.id.send_msg_btn);

        mSocket.on("receive_msg_send") { args ->
            if (args[0] != null) {
                val msgsock = args[0]
                if(msgsock != null){
                    val jsonMsg=JSONObject(msgsock.toString())
                    val user=User(jsonMsg.getString("user_id"),currentUser_username,"0","0","0",jsonMsg.getString("user_image"),"0")
                    val msg=message("12",jsonMsg.getString("chat_id"),user,jsonMsg.getString("content"),Date())
                    Log.i("receive socket",msg.content)
                    listMsg.add(msg)
                    MainScope().launch {
                        withContext(context = Dispatchers.Default) {
                            this@Chat
                        }
                        recycler_msgs.adapter?.notifyDataSetChanged()
                        recycler_msgs.scrollToPosition(listMsg.indexOf(msg))
                        content.text?.clear()

                    }

                }
                /*runOnUiThread {
                    // The is where you execute the actions after you receive the data
                }*/
            }
        }



        send.setOnClickListener {
            val map=HashMap<String,String>();
            map.put("chat_id",intent.getStringExtra("chat_id").toString())
            map.put("user_id",currentUser_id)
            map.put("content",content.text.toString())
            val user=User(currentUser_id,currentUser_username,"0","0","0",currentUser_image,"0")
            val msg=message("12",intent.getStringExtra("chat_id").toString(),user,content.text.toString(),Date())
            val retrofitBuilder= ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.sendMessage(map)
            retrofitData.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    if (response.isSuccessful){
                        //send data json
                        val map=HashMap<String,String>()
                        map.put("chat_id",msg.chat_id)
                        map.put("content",msg.content)
                        map.put("user_id",msg.user.id)
                        map.put("user_image",msg.user.image)
                        val jsonData:JSONObject=JSONObject(map as Map<String, String>?)
                        //send msg for all user ( socket ) (hné emit / node on)
                        mSocket.emit("send_chat", jsonData)

                    }else{
                        Log.e("fail response",response.body().toString())
                    }

                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Log.e("onFailure_msg",t.toString())
                }
            })
        }

        close.setOnClickListener {
            SocketHandler.closeConnection()
            this.finish()
        }
    }
}