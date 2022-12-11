package com.example.studentchat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.viewpager.widget.ViewPager
import com.example.studentchat.R
import com.example.studentchat.adapters.ViewPagerAdapter
import com.example.studentchat.fragments.Contacts
import com.example.studentchat.fragments.Invitation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)


        //val container=findViewById<FragmentContainerView>(R.id.fragment_container_contact)
        val container=findViewById<ViewPager>(R.id.pager_container)
        val tabs=findViewById<TabLayout>(R.id.tabs_contacts)
        setUpTabs(container,tabs)
        //tabs.addOnTabSelectedListener(TabLayout.OnTabSelectedListener{})
        //replaceFragment(Invitation(this));
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

    private fun setUpTabs(container: ViewPager, tabs: TabLayout) {
        val adapter= ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Contacts(this),"Amis")
        adapter.addFragment(Invitation(this),"Invitations")
        container.adapter=adapter
        tabs.setupWithViewPager(container)
    }
}