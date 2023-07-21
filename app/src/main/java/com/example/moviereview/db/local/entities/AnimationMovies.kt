package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tblAnimationMovies",indices = [Index(value = ["movieId"], unique = true)], foreignKeys = [ForeignKey(Movies::class,parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class AnimationMovies(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int
)
{

}
