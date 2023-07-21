package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblReviews", foreignKeys = [ForeignKey(Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class Reviews(
    val email : String = "",
    val name : String,
    val movieName : String,
    val movieId: Int,
    val date:String,
    val content:String,
    val rating:Float,
) {
    @PrimaryKey(autoGenerate = true)
    var reviewId:Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reviews

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}