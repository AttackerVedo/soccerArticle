package com.attackervedo.soccerboard.FB

import com.attackervedo.soccerboard.dataModel.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FButils {

    companion object{
        private lateinit var auth : FirebaseAuth

        fun getUid():String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

        fun getTime() : String {
            val currentDateTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
            return dataFormat
        }

//        suspend fun getMyNick(): String = withContext(Dispatchers.IO) {
//            val uid = getUid()
//            val snapshot = FBRef.userInfoRef.child(uid).get().await()
//            val userData = snapshot.getValue(UserData::class.java)
//            userData?.userNickname.toString()
//        }

    }
}