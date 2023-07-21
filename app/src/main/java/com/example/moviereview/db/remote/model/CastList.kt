package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class CastList(
    @SerializedName("id")
    val movieId:Int,
    @SerializedName("cast")
    val results:List<CastDesc>
)
