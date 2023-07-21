package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.UserMovies
import com.example.moviereview.db.local.repositories.UserMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserMoviesViewModel(application: Application) : AndroidViewModel(application) {

    var isAsc = true
    var isDsc = false
    var isName = true
    var isRating = false
    private val repository : UserMoviesRepository

    lateinit var userMoviesByBoth : LiveData<List<UserMovies>>
    lateinit var userMoviesByEmail : LiveData<List<UserMovies>>
    lateinit var userMoviesByMovieId : LiveData<List<UserMovies>>

    init{
        val userMoviesDao = MovieDatabase.getDatabase(application)?.userMoviesDao()
        repository = UserMoviesRepository(userMoviesDao!!)
    }

    fun addUserMovies(userMovies: UserMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserMovie(userMovies)
        }
    }

    fun getUserMoviesByBoth(email:String,movieId:Int)
    {
        userMoviesByBoth = repository.getUserMoviesByBoth(email,movieId)
    }

    fun getUserMoviesByEmail(email: String)
    {
        userMoviesByEmail = repository.getUserMoviesByEmail(email)
    }

    fun getUserMoviesByMovieId(movieId: Int)
    {
        userMoviesByMovieId = repository.getUserMoviesByMovieId(movieId)
    }
}