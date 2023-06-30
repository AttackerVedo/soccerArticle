package com.attackervedo.soccerboard.FB

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{

        val database = Firebase.database
        val userInfoRef = database.getReference("userInfo")
        val articleRef = database.getReference("article")
        val articleCommentRef = database.getReference("articleComment")

    }
}