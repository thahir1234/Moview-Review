package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblGenres", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class Genres(
    val movieId:Int,
    val genreId:Int,
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
}
