package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.AdventureMoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.AdventureMovies
import com.example.moviereview.db.local.entities.TrendingMovies

class AdventureMoviesRepository (private val adventureMoviesDao: AdventureMoviesDao){

    var allAdventureMovies = adventureMoviesDao.getAllData()

    suspend fun addAdventureMovie(adventureMovie: AdventureMovies)
    {
        adventureMoviesDao.addAdventureMovie(adventureMovie)

    }
}