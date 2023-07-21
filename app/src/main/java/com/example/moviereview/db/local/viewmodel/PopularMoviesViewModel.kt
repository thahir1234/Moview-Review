package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.PopularMoviesDao
import com.example.moviereview.db.local.entities.PopularMovies
import com.example.moviereview.db.local.entities.RecentMovies
import com.example.moviereview.db.local.repositories.PopularMoviesRepository
import com.example.moviereview.db.local.repositories.RecentMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PopularMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : PopularMoviesRepository

     var allPopularMovies : LiveData<List<PopularMovies>>

    init{
        val popularMoviesDao = MovieDatabase.getDatabase(application)?.popularMoviesDao()
        repository = PopularMoviesRepository(popularMoviesDao!!)
        allPopularMovies = repository.allPopularMovies
    }

    fun addPopularMovie(popularMovie: PopularMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addPopularMovie(popularMovie)
        }
    }
}