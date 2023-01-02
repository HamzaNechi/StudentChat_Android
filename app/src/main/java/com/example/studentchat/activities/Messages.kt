package com.example.studentchat.activities


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studentchat.fragments.DiscussionFragment
import com.example.studentchat.R
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.example.studentchat.fragments.InscriptionFragment
import com.example.studentchat.fragments.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Messages : AppCompatActivity() {
    lateinit var currentUser_id:String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        //get user connected
        val sharedPref= this.getSharedPreferences("userConnected", Context.MODE_PRIVATE)
        currentUser_id = sharedPref?.getString("_id","default value").toString()


        val toolbar=findViewById<Toolbar>(R.id.myToolbar);
        setSupportActionBar(toolbar)

        val addGroupeBtn=findViewById<FloatingActionButton>(R.id.floating_action_button);

        addGroupeBtn.setOnClickListener{
            val i=Intent(this,AddGroup::class.java)
            startActivity(i);
        }
        
       replaceFragment(DiscussionFragment());




        //onclick bottomnavigationbar
        val bnb=findViewById<BottomNavigationView>(R.id.menu_bottom);
        /******get menu and update selected*****/
        val menu:Menu=bnb.menu
        menu.findItem(R.id.chat).setChecked(true);
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
                    item.isChecked = true
                    val i= Intent(this,Contact::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }//end bottom navigation bar
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu);
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