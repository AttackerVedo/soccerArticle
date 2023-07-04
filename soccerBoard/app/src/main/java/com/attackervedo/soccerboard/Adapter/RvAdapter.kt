package com.attackervedo.soccerboard.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.Fragment.HomeFragment
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.google.firebase.database.collection.LLRBNode
import org.w3c.dom.Text

class RvAdapter(val itemList: MutableList<ArticleData>) : RecyclerView.Adapter<RvAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return viewHolder(view)
    }

    interface ItemClick{
        fun onClick(viw:View , position: Int)
    }

    var itemClick: ItemClick? = null

    override fun getItemCount(): Int {
        //갯수반환
      return itemList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{ view ->
                itemClick?.onClick(view,position)
            }
        }
        holder.bindItems(itemList[position])
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val articleTitle = itemView.findViewById<TextView>(R.id.itemWriteTitle)
        val articleWriter = itemView.findViewById<TextView>(R.id.itemWriterNickname)
        val articleTime = itemView.findViewById<TextView>(R.id.itemWriteTime)
        val articleHit = itemView.findViewById<TextView>(R.id.itemHitValue)
        val articleComment = itemView.findViewById<TextView>(R.id.itemCommentValue)
        val myUid = FButils.getUid()

        @SuppressLint("ResourceAsColor")
        fun bindItems(item: ArticleData){

            articleTitle.text = item.title.toString()
            articleWriter.text = item.nickname.toString()
            articleTime.text = item.writeTime.toString()
            articleHit.text = item.hit.toString()
            articleComment.text= item.comment.toString()
            if(item.uid!!.equals(myUid)){
                val colorPurple = itemView.context.resources.getColor(R.color.purple)
                articleWriter.setTextColor(colorPurple)
                articleTitle.setTextColor(colorPurple)
            }

        }

    }
}