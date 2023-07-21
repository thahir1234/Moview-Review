package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class MovieList(
    val page:String,
    val results:List<ShortMovieDesc>,
    @SerializedName("total_pages")
    val totalPages:String,

)
