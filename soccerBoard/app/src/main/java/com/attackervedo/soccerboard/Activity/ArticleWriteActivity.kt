package com.attackervedo.soccerboard.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

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

        binding.writeImageAddBtn.setOnClickListener {
            toGoGallery()
        }


    }//onCreate

    private fun imageUpload(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageView = binding.writeImageAddBtn
        val drawable = imageView.drawable

        // 이미지가 있는 경우에만 업로드 수행
        if (drawable != null) {
            val mountainsRef = storageRef.child("${key}.png")

            // Get the data from an ImageView as bytes
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()

            val bitmap: Bitmap? = if (drawable is BitmapDrawable) {
                (drawable as BitmapDrawable).bitmap
            } else {
                null
            }

            if (bitmap != null) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }
            }
        }
        // 이미지가 없는 경우에는 아무 작업도 수행하지 않음
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.writeImageAddBtn.setImageURI(data?.data)
        }
    }

    private fun toGoGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery,100)
    }

    private fun writeAction(nickname: String){

        val title = binding.writeTitleInputEditText.text.toString()
        val content = binding.writeContentInputEditText.text.toString()
        val uid = FButils.getUid()
        val writeTime = FButils.getTime()

        val key = FBRef.articleRef.push().key.toString()

        FBRef.articleRef
            .child(key)
            .setValue(
                ArticleData(
                    title,
                    content,
                    nickname,
                    uid,
                    writeTime,
                    "",
                0,
                0,
                key)
            )
        imageUpload(key)
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