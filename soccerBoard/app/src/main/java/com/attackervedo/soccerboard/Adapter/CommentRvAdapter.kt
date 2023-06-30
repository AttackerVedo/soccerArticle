package com.attackervedo.soccerboard.Adapter

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleCommentData
import com.attackervedo.soccerboard.dataModel.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommentRvAdapter(val commentList:MutableList<ArticleCommentData>):RecyclerView.Adapter<CommentRvAdapter.viewHolder>(){

    private lateinit var nickname:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item_list,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return commentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       holder.bindItem(commentList[position])
    }

    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val itemComment = itemView.findViewById<TextView>(R.id.itemComment)
        val itemCommentTime = itemView.findViewById<TextView>(R.id.itemCommentTime)
        val itemCommentNick = itemView.findViewById<TextView>(R.id.itemCommentNick)
        val itemDeleteBtn = itemView.findViewById<ImageView>(R.id.commentDeleteBtn)


        fun bindItem(articleCommentData: ArticleCommentData) {
                itemComment.text = articleCommentData?.commentContent.toString()
                itemCommentTime.text = articleCommentData?.commentTime.toString()
                itemCommentNick.text = articleCommentData?.commentNickName.toString()
            val commentKey = articleCommentData.commentKey.toString()
            val articleKey :String = articleCommentData.articleKey.toString()
            val myUid = FButils.getUid()
            if(articleCommentData.userUid!!.equals(myUid)){

            }else{
                itemDeleteBtn.visibility = View.GONE
            }

            itemDeleteBtn.setOnClickListener {

                val dialogBuilder = AlertDialog.Builder(itemView.context)
                dialogBuilder.setTitle("댓글 삭제 안내문")
                dialogBuilder.setMessage("댓글을 삭제하시겠습니까?")

                dialogBuilder.setPositiveButton("네") { dialog: DialogInterface, which: Int ->
                    // "네" 버튼을 클릭한 경우 처리할 내용
                    FBRef.articleCommentRef.child(articleKey).child(commentKey).removeValue()

                }
                dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
                    // "아니요" 버튼을 클릭한 경우 처리할 내용
                    dialog.dismiss()
                }

                val dialog = dialogBuilder.create()
                dialog.show()

            }

        }



    }


}