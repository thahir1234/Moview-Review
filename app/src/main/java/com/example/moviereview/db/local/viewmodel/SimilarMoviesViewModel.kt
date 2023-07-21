package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.SimilarMovies
import com.example.moviereview.db.local.repositories.SimilarMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SimilarMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : SimilarMoviesRepository

    lateinit var similarMovies: LiveData<List<SimilarMovies>>
    lateinit var allSimilarMovies: LiveData<List<SimilarMovies>>
    init{
        val similarMoviesDao = MovieDatabase.getDatabase(application)?.similarMoviesDao()
        repository = SimilarMoviesRepository(similarMoviesDao!!)
    }

    fun addSimilarMovie(similarMovie: SimilarMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSimilarMovie(similarMovie)
        }
    }

    fun readAllSimilarMovies()
    {
        allSimilarMovies = repository.readAllSimilarMovies()
    }
    fun getSimilarMovies(movieId:Int)
    {
        similarMovies = repository.getSimilarMovies(movieId)
    }
}