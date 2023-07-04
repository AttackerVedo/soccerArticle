package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.attackervedo.soccerboard.Adapter.MyArticleRvAdapter
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.databinding.ActivityMyArticleListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyArticleListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyArticleListBinding
    private val myArticleList = mutableListOf<ArticleData>()
    private lateinit var myArticleAdapter : MyArticleRvAdapter
    private lateinit var uid:String
    private lateinit var articleValue:String
//    private lateinit var articleValue:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyArticleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uid = FButils.getUid()
        getMyArticle()
        myArticleAdapter = MyArticleRvAdapter(this,myArticleList)
        binding.myArticleRecyclerView.adapter = myArticleAdapter
        binding.myArticleRecyclerView.layoutManager = LinearLayoutManager(this)
//        Log.d("확인1",articleValue)

        myArticleAdapter.itemClick = object : MyArticleRvAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                var clickedArticleData = myArticleList[position]
                val intent = Intent(this@MyArticleListActivity,ArticleDetailActivity::class.java)
                intent.putExtra("clickedArticleData", clickedArticleData)
                startActivity(intent)
            }
        }

        binding.myArticleBackBtn.setOnClickListener {
            val intent = Intent(this@MyArticleListActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }//onCreate

    private fun getMyArticle(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myArticleList.clear()
                for(dataModel in dataSnapshot.children){
                    val articleData = dataModel.getValue(ArticleData::class.java)
                    Log.e("ArticleData", articleData.toString())
                    if(articleData?.uid.equals(uid)){
                        articleData?.let { myArticleList.add(it) }
                    }
                }
                myArticleList.reverse()
                articleValue = myArticleList.size.toString()
                binding.myArticleValue.text = articleValue
                myArticleAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.articleRef.addValueEventListener(postListener)
    }
}//finish