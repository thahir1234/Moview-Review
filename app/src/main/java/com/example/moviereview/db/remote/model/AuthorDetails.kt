package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    val name : String,
    @SerializedName("username")
    var userName : String,
    @SerializedName("avatar_path")
    val profilePic : String,
    val rating : Float,
)
