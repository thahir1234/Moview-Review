package com.example.moviereview.db.local.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.entities.Movies

class MoviesRepository(private val moviesDao: MoviesDao) {

    lateinit var partMovie: LiveData<List<Movies>>

    var allMovies = moviesDao.getAllMovies()
    var allMoviesNameAsc = moviesDao.getAllMoviesNameAsc()
    var allMoviesNameDesc = moviesDao.getAllMoviesNameDesc()
    var allMoviesRatingAsc = moviesDao.getAllMoviesRatingAsc()
    var allMoviesRatingDesc = moviesDao.getAllMoviesRatingDesc()

    fun getParticularMovie(movieId:Int) : LiveData<List<Movies>>
    {
       partMovie =  moviesDao.getParticularMovie(movieId)
        return partMovie
    }
    suspend fun addMovie(movie:Movies)
    {
        moviesDao.addMovie(movie)

    }

    suspend fun updateReview(newRating:Float,newCount:Int,movieId: Int)
    {
        moviesDao.updateReview(newRating,newCount,movieId)
    }
}