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
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.ArticleCommentData
import com.attackervedo.soccerboard.dataModel.ArticleData
import com.attackervedo.soccerboard.dataModel.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommentRvAdapter(val commentList:MutableList<ArticleCommentData>):RecyclerView.Adapter<CommentRvAdapter.viewHolder>(){

//    private lateinit var nickname:String

    private lateinit var articleData: ArticleData

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
                    CustomToast.showToast(itemView.context,"댓글이 삭제되었습니다.")
                    // article 댓글 수 감소

                    getCommentValue(articleKey) { commentValue ->
                        // 실제 값을 사용하는 로직을 이곳에서 처리
                        // 예: commentValue 값을 TextView에 설정하거나 다른 작업 수행
                        // 이 부분은 콜백 내부에서 실행됨
//                        Log.e("받아온 commentValue", commentValue.toString())
//                        Log.e("여기서의 아티클데이터",articleData.toString())
                        val currentCommetValue = commentValue -1

                        FBRef.articleRef
                            .child(articleData.articleKey.toString())
                            .setValue(ArticleData(
                                articleData.title,
                                articleData.content,
                                articleData.nickname,
                                articleData.uid,
                                articleData.writeTime,
                                articleData.updateTime,
                                articleData.hit,
                                currentCommetValue,
                                articleData.articleKey
                            ))
                    }


                }
                dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
                    // "아니요" 버튼을 클릭한 경우 처리할 내용
                    dialog.dismiss()
                }

                val dialog = dialogBuilder.create()
                dialog.show()

            }

        }

        private fun getCommentValue(key: String, callback: (Int) -> Unit) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    articleData = dataSnapshot.getValue(ArticleData::class.java)!!
                    val commentValue = articleData.comment!!
//                    Log.e("가져온코멘트", commentValue.toString())
//                    Log.e("가져온아티클데이터", articleData.toString())
                    callback(commentValue) // 콜백으로 실제 값을 전달
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(0) // 실패한 경우에도 콜백 호출
                }
            }
            FBRef.articleRef.child(key).addListenerForSingleValueEvent(postListener)
        }


    }


}