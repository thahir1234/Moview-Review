package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.ActionMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.ActionMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActionMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : ActionMoviesRepository

    var allActionMovies : LiveData<List<ActionMovies>>

    init {
        val actionMoviesDao = MovieDatabase.getDatabase(application)?.actionMoviesDao()
        repository = ActionMoviesRepository(actionMoviesDao!!)
        allActionMovies = repository.allActionMovies
    }

    fun addActionMovie(actionMovie:ActionMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addActionMovie(actionMovie)
        }
    }
}