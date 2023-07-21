package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblCasts", foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Movies::class, parentColumns = ["movieId"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE)])
data class Casts(
    val castId : Int,
    val movieId:Int,
    val profilePic : String?,
    val castName : String,
    val characterName : String
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Casts

        if (sNo != other.sNo) return false

        return true
    }

    override fun hashCode(): Int {
        return sNo
    }
}
