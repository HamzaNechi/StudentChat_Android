package com.example.studentchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentchat.R
import com.example.studentchat.adapters.ChatListAdapter
import com.example.studentchat.entity.User


class DiscussionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_discussion, container, false);
        val recyclerMsg=view.findViewById<RecyclerView>(R.id.chat_recycler);
        recyclerMsg.layoutManager=LinearLayoutManager(this.context?.let { it},RecyclerView.VERTICAL,false);
        val u1:User=User(nom = "Hamza nechi", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.u3)
        val u2:User=User(nom = "Dhif ghassen", time = "06:12", msg = "sous-espèce issue de la domestication Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.ic_annie)
        val u3:User=User(nom = "Rania toumi", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.u4)
        val u4:User=User(nom = "Sawssen ghrib", time = "06:12", msg = "sous-espèce issue de la domestication sous-espèce issue de la domestication", img = R.drawable.u2)

        val u5:User=User(nom = "Hamza nechi", time = "06:12", msg = "Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.ic_ahri)
        val u6:User=User(nom = "Dhif ghassen", time = "06:12", msg = "sous-espèce issue de la domestication Le Chat domestique (Felis silvestris catus) est la sous-espèce issue de la domestication", img = R.drawable.ic_annie)
        val listUser=ArrayList<User>();
        listUser.add(u1)
        listUser.add(u2)
        listUser.add(u3)
        listUser.add(u4)
        listUser.add(u5)
        listUser.add(u6)



        recyclerMsg.adapter=ChatListAdapter(listUser)
        // Inflate the layout for this fragment
        return view
    }

}