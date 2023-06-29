package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.databinding.ActivityArticleDetailBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArticleDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var articleData = intent.getSerializableExtra("clickedArticleData") as ArticleData
        var key = articleData.articleKey.toString()

        binding.detailTitle.text = articleData.title.toString()
        binding.detailTime.text ="작성시간 : ${articleData.time.toString()}"
        binding.detailContent.text = articleData.content.toString()
        binding.detailNickname.text = "작성자 : ${articleData.nickname.toString()}"

        binding.detailBackBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        getArticleImage(key)

    }

    private fun getArticleImage(key:String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("${key}.png")

// ImageView in your Activity
        val imageView = binding.detailImage

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this).load(task.result).into(imageView)
            }else{
                imageView.isVisible = false
            }
        })


    }
}