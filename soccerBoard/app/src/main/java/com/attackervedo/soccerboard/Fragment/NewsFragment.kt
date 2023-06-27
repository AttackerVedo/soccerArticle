package com.attackervedo.soccerboard.Fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.databinding.FragmentHomeBinding
import com.attackervedo.soccerboard.databinding.FragmentNewsBinding
import com.google.android.material.navigation.NavigationView

class NewsFragment : Fragment(){

    private lateinit var binding : FragmentNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewsBinding.inflate(layoutInflater)
        //클릭이벤트여기서하면됌

        return binding.root
    }//onCreateView


    }//finish





