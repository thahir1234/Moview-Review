package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class ReviewApi(
    val id : String,
    @SerializedName("author")
    val name:String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetails,
    val content : String,
    @SerializedName("created_at")
    val createdDate : String
)
