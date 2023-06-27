package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding : ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introJoinBtn.setOnClickListener {
            val intent = Intent(this@IntroActivity, JoinActivity::class.java)
            startActivity(intent)

        }
        binding.introLoginBtn.setOnClickListener {
            val intent = Intent(this@IntroActivity,LoginActivity::class.java)
            startActivity(intent)

        }
    }//onCreate
}//finish