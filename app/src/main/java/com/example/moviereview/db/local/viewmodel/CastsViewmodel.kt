package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Casts
import com.example.moviereview.db.local.entities.SimilarMovies
import com.example.moviereview.db.local.repositories.CastsRepository
import com.example.moviereview.db.local.repositories.SimilarMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CastsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : CastsRepository

    lateinit var casts: LiveData<List<Casts>>
    lateinit var allCasts: LiveData<List<Casts>>
    init{
        val castsDao = MovieDatabase.getDatabase(application)?.castsDao()
        repository = CastsRepository(castsDao!!)
    }

    fun addCast(cast: Casts)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCast(cast)
        }
    }

    fun getAllCasts()
    {
        allCasts = repository.getAllCasts()
    }
    fun getCasts(movieId:Int)
    {
        casts = repository.getCasts(movieId)
    }
}