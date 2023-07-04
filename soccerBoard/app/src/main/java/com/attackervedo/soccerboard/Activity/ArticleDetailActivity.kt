package com.attackervedo.soccerboard.Activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.attackervedo.soccerboard.Adapter.CommentRvAdapter
import com.attackervedo.soccerboard.Adapter.RvAdapter
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleCommentData
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.dataModel.UserData
import com.attackervedo.soccerboard.databinding.ActivityArticleDetailBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.properties.Delegates

class ArticleDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityArticleDetailBinding
    private lateinit var key :String
    private lateinit var nickname:String
    private lateinit var commentRvAdapter: CommentRvAdapter
    private val commentList = mutableListOf<ArticleCommentData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var articleData = intent.getSerializableExtra("clickedArticleData") as ArticleData
        key = articleData.articleKey.toString()

        binding.detailTitle.text = articleData.title.toString()
        binding.detailWriteTime.text ="작성시간 : ${articleData.writeTime.toString()}"
        if(articleData.updateTime != ""){
            binding.detailUpdateTime.text = "수정시간 : ${articleData.updateTime.toString()}"
        }
        else {
          binding.detailUpdateTime.text = "수정시간 :"
        }
        binding.detailContent.text = articleData.content.toString()
        binding.detailNickname.text = "작성자 : ${articleData.nickname.toString()}"

        binding.detailBackBtn.setOnClickListener {
            finish()
        }

        getArticleImage(key)

        binding.detailDeleteBtn.setOnClickListener {
            deleteArticle()
        }

        binding.detailUpdateBtn.setOnClickListener {
            CustomToast.showToast(this,"수정페이지로 이동합니다")
            val intent = Intent(this, ArticleUpdateActivity::class.java)
            intent.putExtra("articleData", articleData)
            startActivity(intent)
        }

        val myUid = FButils.getUid()
        val writerUid = articleData.uid
        if(myUid.equals(writerUid)){

        }else{
            binding.detailBtnLinearLayout.visibility = View.GONE
        }

        getMyNick()

        binding.detailCommentWriteBtn.setOnClickListener {
            insertComment(articleData)
        }

        commentRvAdapter = CommentRvAdapter(commentList)
        binding.detailCommentRecyclerView.adapter = commentRvAdapter
        binding.detailCommentRecyclerView.layoutManager = LinearLayoutManager(this)
        getArticleCommentData()

        binding.detailImage.setOnClickListener {
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("imageKey", key)
            startActivity(intent)
        }

    }//onCreate

    private fun insertComment(article:ArticleData){

        if( binding.detailComment.text.toString() == ""){
            CustomToast.showToast(this@ArticleDetailActivity,"댓글란이 비었습니다.")
        }else{
            val commentkey = FBRef.articleCommentRef.push().key.toString()
            FBRef.articleCommentRef
                .child(article.articleKey.toString())
                .child(commentkey)
                .setValue(ArticleCommentData(
                    binding.detailComment.text.toString(),
                    nickname,
                    FButils.getTime(),
                    commentkey,
                    article.articleKey.toString(),
                    FButils.getUid()
                ))
            //게시글의 댓글수 증가
            var currentCommentValue =getCommentValue()

            currentCommentValue++
//            Log.e("코멘트", commentValue.toString())
            FBRef.articleRef
                .child(article.articleKey.toString())
                .setValue(ArticleData(
                    article.title,
                    article.content,
                    article.nickname,
                    article.uid,
                    article.writeTime,
                    article.updateTime,
                    article.hit,
                    currentCommentValue,
                    article.articleKey
                ))

            CustomToast.showToast(this,"댓글이 입력되었습니다.")
            binding.detailComment.setText("")
        }
    }

    private fun getCommentValue():Int{

        var commentValue = 0
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            val articleData = dataSnapshot.getValue(ArticleData::class.java)
                commentValue = articleData!!.comment!!
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.articleRef.child(key).addListenerForSingleValueEvent(postListener)
        return commentValue

    }

    private fun deleteArticle() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("글 삭제 안내문")
        dialogBuilder.setMessage("글을 삭제하시겠습니까?")

        dialogBuilder.setPositiveButton("네") { dialog: DialogInterface, which: Int ->
            // "네" 버튼을 클릭한 경우 처리할 내용
           FBRef.articleRef.child(key).removeValue()
            deleteImage(key)
            CustomToast.showToast(this,"글이 삭제 되었습니다.")
            finish()
        }
        dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
            // "아니요" 버튼을 클릭한 경우 처리할 내용
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
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

    private fun deleteImage(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("${key}.png")

        // 이미지 삭제
        imageRef.delete()
            .addOnSuccessListener {
                // 이미지 삭제 성공
            }
            .addOnFailureListener { exception ->
                // 이미지 삭제 실패
            }
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

    private fun getArticleCommentData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()

                for(dataModel in dataSnapshot.children){
                    val articleCommentData = dataModel.getValue(ArticleCommentData::class.java)
                    articleCommentData?.let { commentList.add(it) }
                }

                commentRvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.articleCommentRef.child(key).addValueEventListener(postListener)
    }





}//finish