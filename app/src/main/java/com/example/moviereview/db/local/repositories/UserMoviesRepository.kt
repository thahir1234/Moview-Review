package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.UserMoviesDao
import com.example.moviereview.db.local.entities.UserMovies

class UserMoviesRepository(private val userMoviesDao: UserMoviesDao) {
    lateinit var userMoviesByBoth : LiveData<List<UserMovies>>
    lateinit var userMoviesByEmail : LiveData<List<UserMovies>>
    lateinit var userMoviesByMovieId : LiveData<List<UserMovies>>

    suspend fun addUserMovie(userMovies: UserMovies)
    {
        userMoviesDao.addUserMovie(userMovies)
    }

    fun getUserMoviesByBoth(email:String,movieId:Int) : LiveData<List<UserMovies>>
    {
        userMoviesByBoth = userMoviesDao.getUserMoviesByBoth(email,movieId)
        return userMoviesByBoth
    }

    fun getUserMoviesByEmail(email: String) : LiveData<List<UserMovies>>
    {
        userMoviesByEmail = userMoviesDao.getUserMoviesByEmail(email)
        return userMoviesByEmail
    }

    fun getUserMoviesByMovieId(movieId: Int) : LiveData<List<UserMovies>>
    {
        userMoviesByMovieId = userMoviesDao.getUserMoviesByMovieId(movieId)
        return userMoviesByMovieId
    }
}