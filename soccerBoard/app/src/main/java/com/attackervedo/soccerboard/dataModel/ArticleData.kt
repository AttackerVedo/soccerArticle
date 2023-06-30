package com.attackervedo.soccerboard.dataModel

import java.io.Serializable

data class ArticleData(
    val title :String? = null,
    val content : String? = null,
    val nickname:String? = null,
    val uid : String? = null,
    val writeTime : String? = null,
    val updateTime: String? = null,
    val hit:Long? = 0L,
    val comment :Int? = 0,
    val articleKey : String? = null
) : Serializable

