package com.attackervedo.soccerboard.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.databinding.ActivityShowPhotoBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ShowPhotoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val photoView = binding.showPhoto

        var imageKey = intent.getStringExtra("imageKey")

        getArticleImage(imageKey!!)




    }//onCreate

    private fun getArticleImage(key:String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("${key}.png")

        // ImageView in your Activity
        val imageView = binding.showPhoto

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this).load(task.result).into(imageView)
            }else{
                imageView.isVisible = false
            }
        })


    }
}//finish