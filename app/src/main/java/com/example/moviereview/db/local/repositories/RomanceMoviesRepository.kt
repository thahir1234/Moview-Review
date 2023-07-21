package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.RomanceMoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.RomanceMovies
import com.example.moviereview.db.local.entities.TrendingMovies

class RomanceMoviesRepository (private val romanceMoviesDao: RomanceMoviesDao){

    var allRomanceMovies = romanceMoviesDao.getAllData()

    suspend fun addRomanceMovie(romanceMovie: RomanceMovies)
    {
        romanceMoviesDao.addRomanceMovie(romanceMovie)

    }
}