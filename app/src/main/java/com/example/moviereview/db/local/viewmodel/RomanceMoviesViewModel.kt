package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.RomanceMoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.RomanceMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.RomanceMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RomanceMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : RomanceMoviesRepository

    var allRomanceMovies : LiveData<List<RomanceMovies>>

    init {
        val romanceMoviesDao = MovieDatabase.getDatabase(application)?.romanceMoviesDao()
        repository = RomanceMoviesRepository(romanceMoviesDao!!)
        allRomanceMovies = repository.allRomanceMovies
    }

    fun addRomanceMovie(romanceMovie:RomanceMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addRomanceMovie(romanceMovie)
        }
    }
}