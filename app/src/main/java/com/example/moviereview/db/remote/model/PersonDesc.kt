package com.example.moviereview.db.remote.model

import com.google.gson.annotations.SerializedName

data class PersonDesc(
    @SerializedName("id")
    val personId : Int,
    @SerializedName("also_known_as")
    val realNames:ArrayList<String>,
    val biography : String,
    val birthday : String,
    val deathday : String,
    @SerializedName("name")
    val actingName : String,
    @SerializedName("known_for_department")
    val department:String,
    @SerializedName("profile_path")
    val profilePic : String,
    @SerializedName("place_of_birth")
    val birthPlace : String,
    val gender: Int
)
