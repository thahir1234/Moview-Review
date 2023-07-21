package com.example.moviereview.db.local.repositories

import com.example.moviereview.db.local.dao.ActionMoviesDao
import com.example.moviereview.db.local.entities.ActionMovies

class ActionMoviesRepository (private val actionMoviesDao: ActionMoviesDao){

    var allActionMovies = actionMoviesDao.getAllData()

    suspend fun addActionMovie(actionMovie: ActionMovies)
    {
        actionMoviesDao.addActionMovies(actionMovie)
    }
}