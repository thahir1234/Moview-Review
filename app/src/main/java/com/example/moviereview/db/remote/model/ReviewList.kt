package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class ReviewList (
    val id:Int,
    val results:List<ReviewApi>,
    @SerializedName("total_pages")
    val totalPages:String,
    @SerializedName("total_results")
    val totalResults : String
    ){
}