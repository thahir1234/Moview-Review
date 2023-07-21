package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblSimilarMovies", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class SimilarMovies(
    val movieId:Int,
    val similarMovieId:Int
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
}
