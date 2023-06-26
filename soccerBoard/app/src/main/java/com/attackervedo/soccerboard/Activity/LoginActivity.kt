package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBackBtn.setOnClickListener {
            backBtn()
        }


    }//onCreate

    private fun backBtn(){
        val intent = Intent(this@LoginActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}