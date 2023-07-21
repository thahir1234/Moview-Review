package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.FavouriteMoviesDao
import com.example.moviereview.db.local.entities.Casts
import com.example.moviereview.db.local.entities.FavouriteMovies

class FavMoviesRepository (private val favouriteMoviesDao: FavouriteMoviesDao) {

    lateinit var favMovies: LiveData<List<FavouriteMovies>>

    lateinit var favMoviesById : LiveData<List<FavouriteMovies>>
    suspend fun addFavMovie(favMovie: FavouriteMovies)
    {
        favouriteMoviesDao.addFavMovie(favMovie)
    }


    fun getFavMovies(email:String) : LiveData<List<FavouriteMovies>>
    {
        favMovies = favouriteMoviesDao.getFavMovies(email)
        return favMovies
    }

    fun getFavMoviesById(movieId: Int) : LiveData<List<FavouriteMovies>>
    {
        favMoviesById = favouriteMoviesDao.getFavMoviesById(movieId)
        return favMoviesById
    }
    suspend fun deleteFavMovie(email: String,movieId:Int)
    {
        favouriteMoviesDao.deleteFavMovie(email,movieId)
    }
}