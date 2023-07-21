package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.CrimeMoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.CrimeMovies
import com.example.moviereview.db.local.entities.TrendingMovies

class CrimeMoviesRepository (private val crimeMoviesDao: CrimeMoviesDao){

    var allCrimeMovies = crimeMoviesDao.getAllData()

    suspend fun addCrimeMovie(crimeMovie: CrimeMovies)
    {
        crimeMoviesDao.addCrimeMovie(crimeMovie)
    }
}