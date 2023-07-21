package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblLikedReviews", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE),ForeignKey(
    com.example.moviereview.db.local.entities.Reviews::class, parentColumns = ["reviewId"], childColumns = ["reviewId"], onDelete = ForeignKey.CASCADE)])
data class LikedReviews(
    val email:String,
    val reviewId:Int
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
}
