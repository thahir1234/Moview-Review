package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class LongMovieDesc(
    @SerializedName("id")
    val movieId:Int,
    @SerializedName("backdrop_path")
    val banner:String,
    @SerializedName("poster_path")
    val poster:String,
    @SerializedName("title")
    val name:String,
    @SerializedName("vote_average")
    val rating:Float,
    @SerializedName("overview")
    val description:String,
    val runtime:Int,
    @SerializedName("release_date")
    val releaseDate:String,
    val status:String,
    val genres:List<Genre>,
    @SerializedName("vote_count")
    val voteCount:Int
)