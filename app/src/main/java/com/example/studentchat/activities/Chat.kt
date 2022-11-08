package com.example.studentchat.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studentchat.fragments.DiscussionFragment
import com.example.studentchat.R
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val toolbar=findViewById<Toolbar>(R.id.myToolbar);
        setSupportActionBar(toolbar)
        
       replaceFragment(DiscussionFragment());
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
        val search:EditText= menu?.findItem(R.id.search)?.actionView as EditText;
        search.hint="Trouvez votre message..."
        search.setTextColor(R.color.white);
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search-> Toast.makeText(this,"dd",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item)
    }
}