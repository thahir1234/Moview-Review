package com.example.moviereview.db.local.entities

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tblUsers",indices = [Index(value = ["userId"], unique = false)] ,foreignKeys = [ForeignKey(entity = com.example.moviereview.db.local.entities.Accounts::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE)])
data class Users(
    val userId:Int,
    @PrimaryKey(autoGenerate = false)
    val email:String,
    val name:String,
    val bio:String,
    val image: Bitmap,
    val dateCreated:String
)
