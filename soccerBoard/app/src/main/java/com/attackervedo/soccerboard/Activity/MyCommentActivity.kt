package com.attackervedo.soccerboard.Activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.attackervedo.soccerboard.Adapter.MyCommentRvAdapter
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.dataModel.ArticleCommentData
import com.attackervedo.soccerboard.databinding.ActivityMyCommnetBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyCommentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyCommnetBinding
    private lateinit var uid:String
    private lateinit var commentValue:String
    private val myCommentList = mutableListOf<ArticleCommentData>()
    private lateinit var myCommentRvAdapter : MyCommentRvAdapter
    private lateinit var key : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommnetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myCommentBackBtn.setOnClickListener {
            val intent = Intent(this@MyCommentActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        uid = FButils.getUid()


        if (key.isNotEmpty()) {
            getArticleCommentData()
        } else {
            // key 값이 없는 경우 처리 (기본값 또는 오류 처리)
        }


        myCommentRvAdapter = MyCommentRvAdapter(this, myCommentList)
        binding.myCommentRecyclerView.adapter = myCommentRvAdapter
        binding.myCommentRecyclerView.layoutManager = LinearLayoutManager(this)

        myCommentRvAdapter.itemClick = object : MyCommentRvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val clickedArticleData = myCommentList[position]
                val intent = Intent(this@MyCommentActivity, ArticleDetailActivity::class.java)
                // intent.putExtra("clickedArticleData", clickedArticleData)
                startActivity(intent)
            }
        }
    }//onCreate

    private fun getArticleCommentData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                myCommentList.clear()

                for(dataModel in dataSnapshot.children){
                    val articleCommentData = dataModel.getValue(ArticleCommentData::class.java)
                if(articleCommentData?.userUid.equals(uid)){
                    if (articleCommentData != null) {
                        myCommentList.add(articleCommentData)
                    }
                }
                }
                commentValue = myCommentList.size.toString()
                binding.myArticleCommentValue.text = commentValue
                myCommentRvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.articleCommentRef.child(key).addValueEventListener(postListener)
    }

}//finish