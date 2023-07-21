package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.AnimationMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.AnimationMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnimationMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : AnimationMoviesRepository

    var allAnimationMovies : LiveData<List<AnimationMovies>>

    init {
        val animationMoviesDao = MovieDatabase.getDatabase(application)?.animationMoviesDao()
        repository = AnimationMoviesRepository(animationMoviesDao!!)
        allAnimationMovies = repository.allAnimationMovies
    }

    fun addAnimationMovie(animationMovie:AnimationMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addAnimationMovie(animationMovie)
        }
    }
}