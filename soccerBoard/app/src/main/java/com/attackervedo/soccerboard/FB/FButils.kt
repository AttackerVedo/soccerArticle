package com.attackervedo.soccerboard.FB

import com.google.firebase.auth.FirebaseAuth

class FButils {

    companion object{
        private lateinit var auth : FirebaseAuth

        fun getUid():String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
    }
}