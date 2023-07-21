package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.AdventureMoviesDao
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.AdventureMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.AdventureMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdventureMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : AdventureMoviesRepository

    var allAdventureMovies : LiveData<List<AdventureMovies>>

    init {
        val adventureMoviesDao = MovieDatabase.getDatabase(application)?.adventureMoviesDao()
        repository = AdventureMoviesRepository(adventureMoviesDao!!)
        allAdventureMovies = repository.allAdventureMovies
    }

    fun addAdventureMovie(adventureMovie:AdventureMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addAdventureMovie(adventureMovie)
        }
    }
}