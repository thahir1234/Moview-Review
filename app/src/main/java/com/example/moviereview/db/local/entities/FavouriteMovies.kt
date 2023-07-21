package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblFavouriteMovies", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE),ForeignKey(Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class FavouriteMovies(
    val email:String,
    val movieId:Int
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
}
