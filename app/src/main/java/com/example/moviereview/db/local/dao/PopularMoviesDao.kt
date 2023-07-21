package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.PopularMovies

@Dao
interface PopularMoviesDao {

    @Upsert
    suspend fun addPopularMovie(popularMovie: PopularMovies)

    @Query("select * from tblPopularMovies")
    fun getAllData() : LiveData<List<PopularMovies>>

}