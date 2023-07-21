package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.RecentMoviesDao
import com.example.moviereview.db.local.entities.RecentMovies

class RecentMoviesRepository(private val recentMoviesDao: RecentMoviesDao) {

    var allRecentMovies = recentMoviesDao.getAllData()

    suspend fun addRecentMovie(recentMovie: RecentMovies)
    {
        recentMoviesDao.addRecentMovie(recentMovie)

    }
}