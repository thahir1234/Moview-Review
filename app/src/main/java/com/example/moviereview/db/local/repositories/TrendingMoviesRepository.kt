package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.TrendingMovies

class TrendingMoviesRepository (private val trendingMoviesDao: TrendingMoviesDao){

    var allTrendingMovies = trendingMoviesDao.getAllData()

    suspend fun addTrendingMovies(trendingMovie: TrendingMovies)
    {
        trendingMoviesDao.addTrendingMovie(trendingMovie)
    }
}