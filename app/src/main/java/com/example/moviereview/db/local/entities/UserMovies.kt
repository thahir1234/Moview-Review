package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["email","movieId"], tableName = "tblUserMovies", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE),ForeignKey(Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class UserMovies(
    val email:String,
    val movieId:Int,
    val Status : String
)
{

}
