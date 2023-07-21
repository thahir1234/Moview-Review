package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.SimilarMoviesDao
import com.example.moviereview.db.local.entities.SimilarMovies

class SimilarMoviesRepository(private val similarMoviesDao: SimilarMoviesDao) {
     lateinit var similarMovies: LiveData<List<SimilarMovies>>

     suspend fun addSimilarMovie(similarMovie: SimilarMovies)
     {
         similarMoviesDao.addSimilarMovie(similarMovie)
     }

    fun readAllSimilarMovies() : LiveData<List<SimilarMovies>>
    {
        return similarMoviesDao.readAllSimilarMovies()
    }
     fun getSimilarMovies(movieId:Int) : LiveData<List<SimilarMovies>>
     {
         similarMovies = similarMoviesDao.getSimilarMovies(movieId)
         return similarMovies
     }
}