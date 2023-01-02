package com.example.studentchat.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.R
import com.example.studentchat.adapters.ViewPagerAdapter
import com.example.studentchat.fragments.Contacts
import com.example.studentchat.fragments.Groupes
import com.example.studentchat.fragments.Invitation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val toolbar=findViewById<Toolbar>(R.id.myToolbar);
        setSupportActionBar(toolbar)
        //val container=findViewById<FragmentContainerView>(R.id.fragment_container_contact)
        val container=findViewById<ViewPager>(R.id.pager_container)
        val tabs=findViewById<TabLayout>(R.id.tabs_contacts)
        setUpTabs(container,tabs)
        //tabs.addOnTabSelectedListener(TabLayout.OnTabSelectedListener{})
        //replaceFragment(Invitation(this));

        val addGroupeBtn=findViewById<FloatingActionButton>(R.id.floating_action_button);

        addGroupeBtn.setOnClickListener{
            val i=Intent(this,AddGroup::class.java)
            startActivity(i);
        }
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
                    val i= Intent(this,Messages::class.java)
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
        adapter.addFragment(Groupes(this),"Groupes")
        adapter.addFragment(Invitation(this),"Invitations")
        container.adapter=adapter
        tabs.setupWithViewPager(container)
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        // menu?.getItem(R.id.signout)?.icon?.colorFilter = this.getColor(R.color.primary).
        // val search: SearchView = menu?.findItem(R.id.signout)?.actionView as SearchView;
        //search.queryHint="Chercher un article..."
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signout-> signout();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signout() {
        val sharedPref= this.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        val currentUser_id = sharedPref?.getString("_id","default value").toString()
        val i= Intent(this,Login::class.java)
        //Logout with retrofit
        val map=HashMap<String,String>();
        map.put("id",currentUser_id);
        val retrofitBuilder= ApiPostInterface.retrofitBuilder
        val retrofitData=retrofitBuilder.Logout(map);
        retrofitData.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful){
                    Log.i("UserLogout","Access Logout");
                    startActivity(i)
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("onFailure",t.toString())
            }
        })
    }
}