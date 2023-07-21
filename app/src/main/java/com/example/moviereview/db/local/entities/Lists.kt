package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblLists", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE)])
data class Lists(
    @PrimaryKey(autoGenerate = true)
    val listId:Int = 0,
    val email:String,
    val title:String,
    val dateCreated:String,
    val visibility:String
)
