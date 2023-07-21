package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.repositories.ListMoviesRepository
import com.example.moviereview.db.local.repositories.ReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : ListMoviesRepository

    lateinit var listMoviesByListId : LiveData<List<ListMovies>>
    lateinit var moviesById : LiveData<List<ListMovies>>
    lateinit var moviesByBoth : LiveData<List<ListMovies>>
    init {
        val listMoviesDao = MovieDatabase.getDatabase(application)?.listMoviesDao()
        repository = ListMoviesRepository(listMoviesDao!!)
    }

    fun addListMovie(listMovie : ListMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addListMovie(listMovie)
        }
    }

    fun getListMovies(listId:Int)
    {
        listMoviesByListId = repository.getListMovies(listId)
    }

    fun getMoviesById(movieId: Int)
    {
        moviesById = repository.getMoviesById(movieId)
    }

    fun getMoviesByBoth(listId: Int,movieId: Int)
    {
        moviesByBoth = repository.getMoviesByBoth(listId,movieId)
    }

    fun deleteListMovie(listId: Int,movieId: Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.deleteListMovie(movieId,listId)
        }
    }
}