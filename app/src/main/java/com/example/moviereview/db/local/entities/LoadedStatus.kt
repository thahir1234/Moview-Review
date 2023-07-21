package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblLoadedStatus")
class LoadedStatus(
    @PrimaryKey(autoGenerate = false)
    var sid : Int = 0,
    val popularMovies : Int,
    val trendingMovies : Int,
    val recentMovies : Int,
    val actionMovies : Int,
    val adventureMovies : Int,
    val animationMovies : Int,
    val romanceMovies : Int,
    val crimeMovies : Int,
){

}
