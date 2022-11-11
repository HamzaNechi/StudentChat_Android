package com.example.studentchat.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.studentchat.R
import com.example.studentchat.activities.Chat
import com.example.studentchat.activities.Home


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_login, container, false)

        val btnLogin=view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
             //val i= this.context?.let { Intent(it,Home::class.java) }
            val i=Intent(this.context?.let{it},Home::class.java);
            startActivity(i)
        }
        return view
    }
}