package com.example.moviereview.view.list_screen_clean.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.repositories.ListMoviesRepository
import com.example.moviereview.view.list_screen_clean.data.repository.ListMovieRepoImpl
import com.example.moviereview.view.list_screen_clean.domain.usecases.AddListMovie
import com.example.moviereview.view.list_screen_clean.domain.usecases.DeleteListMovie
import com.example.moviereview.view.list_screen_clean.domain.usecases.GetListMovies
import com.example.moviereview.view.list_screen_clean.domain.usecases.GetPartMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : ListMovieRepoImpl

    lateinit var listMoviesByListId : LiveData<List<ListMovies>>
    lateinit var moviesByBoth : LiveData<List<ListMovies>>
    init {
        val listMoviesDao = MovieDatabase.getDatabase(application)?.listMoviesDao()
        repository = ListMovieRepoImpl(listMoviesDao!!)
    }

    fun addListMovie(listMovie : ListMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val addListMovie = AddListMovie(repository)
            addListMovie(listMovie)
        }
    }

    fun getListMovies(listId:Int)
    {
        val getListMovies = GetListMovies(repository)
        listMoviesByListId = getListMovies(listId)
    }


    fun getMoviesByBoth(listId: Int,movieId: Int)
    {
        val getPartMovie = GetPartMovie(repository)
        moviesByBoth = getPartMovie(listId,movieId)
    }

    fun deleteListMovie(listId: Int,movieId: Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val deleteListMovie = DeleteListMovie(repository)
            deleteListMovie(movieId,listId)
        }
    }
}