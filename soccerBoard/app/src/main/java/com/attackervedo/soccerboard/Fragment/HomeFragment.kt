package com.attackervedo.soccerboard.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.attackervedo.soccerboard.Activity.ArticleDetailActivity
import com.attackervedo.soccerboard.Activity.ArticleWriteActivity
import com.attackervedo.soccerboard.Adapter.RvAdapter
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {//finish

    private lateinit var binding : FragmentHomeBinding
    private lateinit var rvAdapter : RvAdapter
    private val articleList = mutableListOf<ArticleData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        //클릭이벤트여기서하면
        binding.homeWriteBtn.setOnClickListener {
            moveWrite()
        }

        getArticleData()
        rvAdapter = RvAdapter( articleList )
        binding.homeFragRv.adapter = rvAdapter
        binding.homeFragRv.layoutManager = LinearLayoutManager(requireContext())

        rvAdapter.itemClick = object : RvAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                var clickedArticleData = articleList[position]
                val intent = Intent(requireContext(),ArticleDetailActivity::class.java)
                intent.putExtra("clickedArticleData", clickedArticleData)
                startActivity(intent)
            }

        }

        return binding.root
    }//onCreateView

    private fun moveWrite(){
        val intent = Intent(requireContext(),ArticleWriteActivity::class.java)
        startActivity(intent)
    }

    private fun getArticleData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                articleList.clear()

                for(dataModel in dataSnapshot.children){
                    val articleData = dataModel.getValue(ArticleData::class.java)
                    Log.e("ArticleData", articleData.toString())
                    articleData?.let { articleList.add(it) }
                }
                articleList.reverse()
                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.articleRef.addValueEventListener(postListener)
    }




}