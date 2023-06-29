package com.attackervedo.soccerboard.dataModel

data class ArticleData(
    val title :String? = null,
    val content : String? = null,
    val nickname:String? = null,
    val uid : String? = null,
    val time : String? = null,
    val hit:Long? = 0L,
    val comment :Int? = 0
)
