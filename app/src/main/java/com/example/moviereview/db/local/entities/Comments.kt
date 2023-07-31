package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblComments", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE)])
data class Comments(
    @PrimaryKey(autoGenerate = true)
    val commentId:Int = 0,
    val email:String,
    val date:String,
    val content:String,
    val likeCount:String,
    val reviewId:Int,
    val listId:Int
)
