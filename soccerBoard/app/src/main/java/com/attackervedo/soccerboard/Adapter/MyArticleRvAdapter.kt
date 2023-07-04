package com.attackervedo.soccerboard.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleData
import org.w3c.dom.Text

class MyArticleRvAdapter(val context: Context,
                         val itemList: MutableList<ArticleData>) :RecyclerView.Adapter<MyArticleRvAdapter.viewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    interface ItemClick{
        fun onClick(view:View , position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{ view ->
                itemClick?.onClick(view,position)
            }
        }

        holder.bindItem(itemList[position])
    }

    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val myArticleTitle = itemView.findViewById<TextView>(R.id.itemWriteTitle)
        val myArticleWriter = itemView.findViewById<TextView>(R.id.itemWriterNickname)
        val myArticleTime = itemView.findViewById<TextView>(R.id.itemWriteTime)
        val myArticleHit = itemView.findViewById<TextView>(R.id.itemHitValue)
        val myArticleComment = itemView.findViewById<TextView>(R.id.itemCommentValue)


        fun bindItem(item: ArticleData){
            myArticleTitle.text = item.title.toString()
            myArticleWriter.text = item.nickname.toString()
            myArticleTime.text = item.writeTime.toString()
            myArticleHit.text = item.hit.toString()
            myArticleComment.text= item.comment.toString()

        }
    }
}