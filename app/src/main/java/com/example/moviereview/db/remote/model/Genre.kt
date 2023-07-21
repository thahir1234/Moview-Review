package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val genreId:Int,
    @SerializedName("name")
    val genreName:String
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Genre

        if (genreId != other.genreId) return false
        if (genreName != other.genreName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = genreId
        result = 31 * result + genreName.hashCode()
        return result
    }
}
