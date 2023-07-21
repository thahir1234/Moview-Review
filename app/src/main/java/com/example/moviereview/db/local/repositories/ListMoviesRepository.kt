package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.ListMoviesDao
import com.example.moviereview.db.local.dao.ReviewsDao
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Reviews

class ListMoviesRepository (private val listMoviesDao: ListMoviesDao) {

    lateinit var listMoviesByListId : LiveData<List<ListMovies>>

    lateinit var moviesById : LiveData<List<ListMovies>>

    lateinit var moviesByBoth : LiveData<List<ListMovies>>

    suspend fun addListMovie(listMovie: ListMovies)
    {
        listMoviesDao.addListMovie(listMovie)
    }


    fun getListMovies(listId:Int) : LiveData<List<ListMovies>>
    {
        listMoviesByListId = listMoviesDao.getAllListMovies(listId)
        return listMoviesByListId
    }

    fun getMoviesById(movieId: Int) : LiveData<List<ListMovies>>
    {
        moviesById = listMoviesDao.getAllMoviesById(movieId)
        return moviesById
    }

    fun getMoviesByBoth(listId: Int,movieId: Int) : LiveData<List<ListMovies>>
    {
        moviesByBoth = listMoviesDao.getMoviesByBoth(listId,movieId)
        return moviesByBoth
    }
    suspend fun deleteListMovie(movieId: Int,listId: Int)
    {
        listMoviesDao.deleteListMovie(movieId,listId)
    }
}