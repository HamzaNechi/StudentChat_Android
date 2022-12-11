package com.example.studentchat.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ListPost
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.activities.Chat
import com.example.studentchat.activities.Home
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var map:HashMap<String,String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_login, container, false)
        email=view.findViewById(R.id.email)
        password=view.findViewById(R.id.password)
        val btnLogin=view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            map=HashMap<String,String>()
            map.put("email",email.text.toString())
            map.put("password",password.text.toString())
            val retrofitBuilder= ApiPostInterface.retrofitBuilder
            val retrofitData=retrofitBuilder.login(map)
            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if (response.isSuccessful){
                        //get user connect
                        val user:User=response.body()!!
                        val sharedPreference =activity?.getSharedPreferences("userConnected",Context.MODE_PRIVATE)
                        var editor = sharedPreference?.edit()
                        editor!!.putString("_id",user.id)
                        editor!!.putString("username",user.username)
                        editor!!.putString("role",user.role)
                        editor!!.putString("email",user.email)
                        editor!!.putString("image",user.image)
                        editor.commit()
                        val i=Intent(it.context,Home::class.java);
                        startActivity(i)
                        Toast.makeText(it.context,
                            sharedPreference!!.getString("username","kaaabboul"),Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(it.context,response.message(),Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("onFailure Login",t.toString())
                }
            })

        }
        return view
    }
}