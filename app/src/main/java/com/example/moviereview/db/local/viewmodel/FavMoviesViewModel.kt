package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Casts
import com.example.moviereview.db.local.entities.FavouriteMovies
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.repositories.CastsRepository
import com.example.moviereview.db.local.repositories.FavMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : FavMoviesRepository

    lateinit var favMovies: LiveData<List<FavouriteMovies>>
    lateinit var favMoviesById : LiveData<List<FavouriteMovies>>
    init{
        val favouriteMoviesDao = MovieDatabase.getDatabase(application)?.favouriteMoviesDao()
        repository = FavMoviesRepository(favouriteMoviesDao!!)
    }

    fun addFavMovie(favMovie: FavouriteMovies)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavMovie(favMovie)
        }
    }

    fun getFavMoviesById(movieId: Int)
    {
        favMoviesById = repository.getFavMoviesById(movieId)
    }

    fun getFavMovies(email:String)
    {
        favMovies = repository.getFavMovies(email)
    }

    fun deleteFavMovie(email: String,movieId:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavMovie(email,movieId)
        }
    }
}