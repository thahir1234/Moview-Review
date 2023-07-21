package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.RecentMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.RecentMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecentMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : RecentMoviesRepository

    var allRecentMovies : LiveData<List<RecentMovies>>
    init {
        val recentMoviesDao = MovieDatabase.getDatabase(application)?.recentMoviesDao()
        repository = RecentMoviesRepository(recentMoviesDao!!)
        allRecentMovies = repository.allRecentMovies
    }

    fun addRecentMovie(recentMovie: RecentMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addRecentMovie(recentMovie)
        }
    }
}