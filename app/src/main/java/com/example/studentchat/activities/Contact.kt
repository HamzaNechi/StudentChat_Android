package com.example.studentchat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.FragmentContainerView
import com.example.studentchat.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)


        val container=findViewById<FragmentContainerView>(R.id.fragment_container_contact)
        val tabs=findViewById<TabLayout>(R.id.tabs_contacts)

        val bnb=findViewById<BottomNavigationView>(R.id.menu_bottom);
        /******get menu and update selected*****/
        val menu: Menu =bnb.menu
        menu.findItem(R.id.contacts).setChecked(true);
        bnb.setOnNavigationItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.home -> {
                    val i= Intent(this,Home::class.java)
                    startActivity(i)
                    true
                }
                R.id.chat -> {
                    item.setChecked(true)
                    val i= Intent(this,Chat::class.java)
                    startActivity(i)
                    true
                }
                R.id.contacts->{
                    item.setChecked(true)
                    val i= Intent(this,Contact::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }
    }
}