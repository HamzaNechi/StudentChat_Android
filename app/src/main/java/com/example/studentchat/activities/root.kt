package com.example.studentchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studentchat.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class root : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val b=findViewById<BottomNavigationView>(R.id.menu_bottom);
    }
}