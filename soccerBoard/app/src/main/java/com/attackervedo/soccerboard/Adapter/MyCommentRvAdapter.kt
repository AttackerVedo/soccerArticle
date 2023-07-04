package com.attackervedo.soccerboard.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleCommentData
import com.attackervedo.soccerboard.dataModel.ArticleData

class MyCommentRvAdapter(
    val context: Context,
    val itemList: MutableList<ArticleCommentData>
) : RecyclerView.Adapter<MyCommentRvAdapter.viewHodler>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHodler {
        var view = LayoutInflater.from(context).inflate(R.layout.my_comment_item,parent,false)
        return viewHodler(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    interface ItemClick{
        fun onClick(view:View , position: Int)
    }

    var itemClick: ItemClick? = null
    override fun onBindViewHolder(holder: viewHodler, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{ view ->
                itemClick?.onClick(view,position)
            }
        }
        holder.itemBind(itemList[position])
    }

    inner class viewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val myItemComment = itemView.findViewById<TextView>(R.id.itemComment)
        val myItemCommentTime = itemView.findViewById<TextView>(R.id.itemCommentTime)
        val myItemCommentNick = itemView.findViewById<TextView>(R.id.itemCommentNick)
//        val myItemDeleteBtn = itemView.findViewById<ImageView>(R.id.commentDeleteBtn)

        fun itemBind(item: ArticleCommentData) {
            myItemComment.text = item.commentContent.toString()
            myItemCommentTime.text = item.commentTime.toString()
            myItemCommentNick.text = item.commentNickName.toString()
        }
    }
}