package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.CrimeMoviesDao
import com.example.moviereview.db.local.dao.MoviesDao
import com.example.moviereview.db.local.dao.TrendingMoviesDao
import com.example.moviereview.db.local.entities.CrimeMovies
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.CrimeMoviesRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CrimeMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : CrimeMoviesRepository

    var allCrimeMovies : LiveData<List<CrimeMovies>>

    init {
        val crimeMoviesDao = MovieDatabase.getDatabase(application)?.crimeMoviesDao()
        repository = CrimeMoviesRepository(crimeMoviesDao!!)
        allCrimeMovies = repository.allCrimeMovies
    }

    fun addCrimeMovie(crimeMovie:CrimeMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            repository.addCrimeMovie(crimeMovie)
        }
    }
}