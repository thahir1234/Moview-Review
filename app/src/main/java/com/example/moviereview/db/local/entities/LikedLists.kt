package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["email","listId"],tableName = "tblLikedLists", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE),ForeignKey(
    com.example.moviereview.db.local.entities.Lists::class, parentColumns = ["listId"], childColumns = ["listId"], onDelete = ForeignKey.CASCADE)])
data class LikedLists(
    val email:String,
    val listId:Int
) {

}