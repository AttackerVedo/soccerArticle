package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.dataModel.UserData
import com.attackervedo.soccerboard.databinding.ActivityArticleWriteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ArticleWriteActivity : AppCompatActivity() {
    private lateinit var nickname:String
    private lateinit var binding : ActivityArticleWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeBackBtn.setOnClickListener {
            val intent = Intent(this@ArticleWriteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        getMyNick()

        binding.writeWriteBtn.setOnClickListener {
            if (::nickname.isInitialized) {
                writeAction(nickname)
            } else {
            }
        }
    }
    private fun writeAction(nickname: String){

        val title = binding.writeTitleInputEditText.text.toString()
        val content = binding.writeContentInputEditText.text.toString()
        val uid = FButils.getUid()
        val time = FButils.getTime()

        FBRef.articleRef
            .push()
            .setValue(
                ArticleData(
                    title,
                    content,
                    nickname,
                    uid,
                    time,
                0,
                0)
            )

        CustomToast.showToast(this@ArticleWriteActivity, "글이 작성되었습니다.")
        finish()
    }

    private fun getMyNick() {
        val uid = FButils.getUid()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(UserData::class.java)
                nickname = userData?.userNickname.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 처리 중 오류가 발생한 경우의 동작을 정의합니다.
            }
        }
        FBRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
}