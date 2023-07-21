package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrendingMovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TrendingMoviesRepository

    var allTrendingMovies : LiveData<List<TrendingMovies>>

    init {
        val trendingMoviesDao = MovieDatabase.getDatabase(application)?.trendingMoviesDao()
        repository = TrendingMoviesRepository(trendingMoviesDao!!)
        allTrendingMovies = repository.allTrendingMovies
    }

    fun addTrendingMovie(trendingMovie:TrendingMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addTrendingMovies(trendingMovie)
        }
    }
}