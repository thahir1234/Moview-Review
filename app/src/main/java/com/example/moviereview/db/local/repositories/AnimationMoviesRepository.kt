package com.example.moviereview.db.local.repositories

import android.util.Log
import com.example.moviereview.db.local.dao.AnimationMoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.AnimationMovies
import com.example.moviereview.db.local.entities.TrendingMovies

class AnimationMoviesRepository (private val animationMoviesDao: AnimationMoviesDao){

    var allAnimationMovies = animationMoviesDao.getAllData()

    suspend fun addAnimationMovie(animationMovie: AnimationMovies)
    {
        animationMoviesDao.addAnimationMovie(animationMovie)

    }
}