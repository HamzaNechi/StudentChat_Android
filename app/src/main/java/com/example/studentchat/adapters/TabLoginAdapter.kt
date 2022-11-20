package com.example.studentchat.adapters

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.studentchat.fragments.LoginFragment

internal class TabLoginAdapter(var context:Context,fm:FragmentManager,val totalTabs:Int):FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                LoginFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs;
    }
}