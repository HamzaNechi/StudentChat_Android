package com.example.studentchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
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

        val u1: User = User(nom = "Hamza nechi", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.u3);

        val p1:Post=Post(image = R.drawable.logo, description = "this is my new post", date = "11/09/2022", u = u1);
        val p2:Post=Post(image = R.drawable.u2, description = "test 2éme post", date = "11/09/2022", u = u1);
        val p3:Post=Post(image = R.drawable.ic_ashe, description = "test 3éme post", date = "11/09/2022", u = u1);

        val listPost=ArrayList<Post>();
        listPost.add(p1)
        listPost.add(p2)
        listPost.add(p3)

        posts.adapter=PostAdapter(listPost);
    }
}