package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.dataModel.UserData
import com.attackervedo.soccerboard.databinding.ActivityMyInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyInfoBinding
    private var uid = FButils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myInfoHomeBtn.setOnClickListener {
            val intent = Intent(this@MyInfoActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        getMydata()
    }

    private fun getMydata(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            val userData = dataSnapshot.getValue(UserData::class.java)
                binding.myinfoEmail.text = userData?.userEmail.toString()
                binding.myInfoNickname.text = userData?.userNickname.toString()

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
}