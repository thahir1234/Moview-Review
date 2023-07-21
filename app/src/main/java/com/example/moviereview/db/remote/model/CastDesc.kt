package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class CastDesc(
    @SerializedName("id")
    val castId:Int,
    val name:String,
    @SerializedName("character")
    val characterName:String,
    @SerializedName("profile_path")
    val castPic:String,
    @SerializedName("known_for_department")
    val department:String
) {
}