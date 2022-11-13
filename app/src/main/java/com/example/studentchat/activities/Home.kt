package com.example.studentchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.R
import com.example.studentchat.adapters.PostAdapter
import com.example.studentchat.entity.Post
import com.example.studentchat.entity.User

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val posts=findViewById<RecyclerView>(R.id.recycler_post);
        posts.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        val u1: User = User(nom = "Hamza nechi", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.u3);
        val u2: User = User(nom = "Dhif ghassen", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.u2);

        val p1:Post=Post(image = R.drawable.bg_top_log, description = "Wikipédia est un projet d’encyclopédie collective en ligne, universelle, multilingue et fonctionnant sur le principe du wiki. Ce projet vise à offrir un contenu librement réutilisable, objectif et vérifiable, que chacun peut modifier et améliorer.", date = "11/09/2022", u = u1);
        val p2:Post=Post(image = R.drawable.u2, description = "\uD83D\uDCCA Show your network speed right in the toolbar Download here: https://rebrand.ly/computernetworkspeed", date = "11/09/2022", u = u2);
        val p3:Post=Post(image = 0,description = "\\uD83D\\uDCCA Show your network speed right in the toolbar Download here: https://rebrand.ly/computernetworkspeed", date = "11/09/2022", u = u1);

        val listPost=ArrayList<Post>();
        listPost.add(p1)
        listPost.add(p2)
        listPost.add(p3)

        posts.adapter=PostAdapter(listPost);
    }
}