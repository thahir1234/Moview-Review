package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.PopularMoviesDao
import com.example.moviereview.db.local.entities.PopularMovies

class PopularMoviesRepository(private val popularMoviesDao: PopularMoviesDao) {

    var allPopularMovies = popularMoviesDao.getAllData()
    suspend fun addPopularMovie(popularMovie: PopularMovies)
    {
        popularMoviesDao.addPopularMovie(popularMovie)

    }
}