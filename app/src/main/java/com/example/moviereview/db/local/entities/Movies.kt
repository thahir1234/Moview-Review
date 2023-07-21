package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblMovies")
data class Movies(
    @PrimaryKey(autoGenerate = false)
    val movieId:Int,
    val movieBanner: String? = null,
    val moviePoster: String? = null,
    val movieName:String,
    val rating:Float,
    val voteCount:Int,
    val description:String,
    val runtime:Int,
    val releaseDate:String,
    val status:String
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movies

        if (movieId != other.movieId) return false

        return true
    }

    override fun hashCode(): Int {
        return movieId
    }
}
