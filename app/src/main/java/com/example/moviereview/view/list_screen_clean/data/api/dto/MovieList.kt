package com.example.moviereview.view.list_screen_clean.data.api.dto

import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.google.gson.annotations.SerializedName

data class MovieList(
    val page:String,
    val results:List<ShortMovieDesc>,
    @SerializedName("total_pages")
    val totalPages:String,

    )
