package com.example.studentchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.example.studentchat.activities.Chat
import com.example.studentchat.activities.Login

class MainActivity : AppCompatActivity() {
    lateinit var handler: Handler;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler=Handler();
        handler.postDelayed({
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
            finish()

        }, 4000);
    }
}