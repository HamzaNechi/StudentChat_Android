package com.example.studentchat.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studentchat.R
import com.example.studentchat.fragments.AddPost
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColor
import com.example.studentchat.Interface.ApiPostInterface
import com.example.studentchat.Interface.ServerResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar);
        val btnAddPost=findViewById<FloatingActionButton>(R.id.floating_action_button);
        toolbar.title="Accueil"
        btnAddPost.visibility=View.VISIBLE
    /**Permission read storage**/
        val permissionsStorage = arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val requestExternalStorage = 1
        val permission =
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage)
        }
        /***End permission read storage**/



        btnAddPost.setOnClickListener {
            toolbar.title="Nouvelle publication";
            toolbar.menu.close()
            btnAddPost.visibility=View.GONE
            replaceFragment(AddPost(this));
        }


        //onclick bottomnavigationbar
        val bnb=findViewById<BottomNavigationView>(R.id.menu_bottom);
        val menu:Menu=bnb.menu
        menu.findItem(R.id.home).setChecked(true);
        //Log.e("Menu item",menu.toString());
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
        fragmentTransaction.addToBackStack(null).commit()
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