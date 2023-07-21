package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "tblListMovies", primaryKeys = ["listId","movieId"], foreignKeys = [ForeignKey(com.example.moviereview.db.local.entities.Lists::class, parentColumns = ["listId"], childColumns = ["listId"], onDelete = ForeignKey.CASCADE)])
data class ListMovies(
    val listId:Int,
    val movieId:Int,
    val poster:String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListMovies

        if (movieId != other.movieId) return false
        if (poster != other.poster) return false

        return true
    }

    override fun hashCode(): Int {
        var result = movieId
        result = 31 * result + poster.hashCode()
        return result
    }
}