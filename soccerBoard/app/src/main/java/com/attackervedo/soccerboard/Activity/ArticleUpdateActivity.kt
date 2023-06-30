package com.attackervedo.soccerboard.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.view.isVisible
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.databinding.ActivityArticleDetailBinding
import com.attackervedo.soccerboard.databinding.ActivityArticleUpdateBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ArticleUpdateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArticleUpdateBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var articleData = intent.getSerializableExtra("articleData") as? ArticleData
            binding.updateTitleInputEditText.setText(articleData!!.title)
            binding.updateContentInputEditText.setText(articleData!!.content)

        val uid = articleData.uid.toString()
        val articleKey = articleData.articleKey.toString()
        getArticleImage(articleKey)


        binding.updateBackBtn.setOnClickListener {
            finish()
        }

        binding.updateImageAddBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)
        }

        binding.updateWriteBtn.setOnClickListener {
            UpdateAction(articleData)
        }
    }//onCreate

    private fun UpdateAction(articleData:ArticleData) {
        FBRef.articleRef
            .child(articleData.articleKey.toString())
            .setValue(ArticleData(
                binding.updateTitleInputEditText.text.toString(),
                binding.updateContentInputEditText.text.toString(),
                articleData.nickname,
                articleData.uid,
                articleData.writeTime,
                FButils.getTime(),
                articleData.hit,
                articleData.comment,
                articleData.articleKey
            ))

        imageUpload(articleData.articleKey.toString())
        CustomToast.showToast(this,"글이 수정되었습니다.")
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getArticleImage(aritcleKey:String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("${aritcleKey}.png")

        // ImageView in your Activity
        val imageView = binding.updateImageAddBtn

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this).load(task.result).into(imageView)
            }else{
                imageView.isVisible = false
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.updateImageAddBtn.setImageURI(data?.data)
        }
    }

    private fun imageUpload(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageView = binding.updateImageAddBtn
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
}//finish