package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.SimilarMovies

@Dao
interface SimilarMoviesDao {
    @Upsert
    suspend fun addSimilarMovie(similarMovie: com.example.moviereview.db.local.entities.SimilarMovies)

    @Query("SELECT * FROM tblSimilarMovies")
    fun readAllSimilarMovies():LiveData<List<com.example.moviereview.db.local.entities.SimilarMovies>>

    @Query("Select * from tblSimilarMovies where movieId = :movieId")
    fun getSimilarMovies(movieId:Int) : LiveData<List<SimilarMovies>>
}