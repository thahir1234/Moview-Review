package com.example.moviereview.db.local.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class  MoviesViewModel (application: Application) : AndroidViewModel(application) {

    private val repository : MoviesRepository

    lateinit var partMovie : LiveData<List<Movies>>

    var allMovies : LiveData<List<Movies>>
    var allMoviesNameAsc : LiveData<List<Movies>>
    var allMoviesNameDesc : LiveData<List<Movies>>
    var allMoviesRatingAsc : LiveData<List<Movies>>
    var allMoviesRatingDesc : LiveData<List<Movies>>
    init {
        val moviesDao = MovieDatabase.getDatabase(application)?.moviesDao()
        repository = MoviesRepository(moviesDao!!)
        allMovies =  repository.allMovies
        allMoviesNameAsc = moviesDao.getAllMoviesNameAsc()
        allMoviesNameDesc = moviesDao.getAllMoviesNameDesc()
        allMoviesRatingAsc = moviesDao.getAllMoviesRatingAsc()
        allMoviesRatingDesc = moviesDao.getAllMoviesRatingDesc()
    }

    fun addMovie(movie:Movies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)

        }
    }

    fun getParticularMovie(movieId:Int) : LiveData<List<Movies>>
    {
        partMovie = repository.getParticularMovie(movieId)
        return partMovie
    }

    fun updateReview(newRating:Float,newCount:Int,movieId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReview(newRating,newCount,movieId)
        }
    }
}