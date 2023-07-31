package com.example.moviereview.view.list_screen_clean.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviereview.db.local.dao.ListMoviesDao
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.view.list_screen_clean.data.api.dto.MovieList
import com.example.moviereview.view.list_screen_clean.data.api.MovieSearchApiImpl
import com.example.moviereview.view.list_screen_clean.domain.repository.ListMovieRepo

class ListMovieRepoImpl
    (private val dao: ListMoviesDao): ListMovieRepo{
    override suspend fun addListMovie(listMovie: ListMovies) {
        dao.addListMovie(listMovie)
    }

    override suspend fun deleteListMovie(movieId: Int, listId: Int) {
        dao.deleteListMovie(movieId,listId)
    }

    override fun getListMovies(listId: Int): LiveData<List<ListMovies>> {
        return dao.getAllListMovies(listId)
    }

    override fun getPartMovie(listId: Int, movieId: Int): LiveData<List<ListMovies>> {
        return dao.getMoviesByBoth(listId,movieId)
    }

    override fun searchMovie(query: String): MutableLiveData<MovieList>? {
        return MovieSearchApiImpl.searchMovies(query.trim())
    }
}