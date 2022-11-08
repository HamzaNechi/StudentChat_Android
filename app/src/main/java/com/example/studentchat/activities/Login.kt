package com.example.studentchat.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.studentchat.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin=findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val i=Intent(this,Chat::class.java)
            startActivity(i)
        }
    }
}