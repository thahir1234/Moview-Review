package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface TrendingMoviesDao {

    @Upsert
    suspend fun addTrendingMovie(trendingMovie: TrendingMovies)

    @Query("select * from tblTrendingMovies")
    fun getAllData() : LiveData<List<TrendingMovies>>

}