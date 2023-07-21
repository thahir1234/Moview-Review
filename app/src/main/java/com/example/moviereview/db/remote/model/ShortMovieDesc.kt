package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class ShortMovieDesc(
    @SerializedName("id")
    val movieId:Int,
    @SerializedName("title")
    val name:String,
    @SerializedName("vote_average")
    val rating:Float= 0.0f,
    @SerializedName("release_date")
    val releaseDate:String = "",
    @SerializedName("poster_path")
    val poster:String = "",
    @SerializedName("backdrop_path")
    val banner:String ="",
    @SerializedName("vote_count")
    val voteCount: Int = 0

)
