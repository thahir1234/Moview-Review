package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.CrimeMovies
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface CrimeMoviesDao {

    @Upsert
    suspend fun addCrimeMovie(crimeMovie: CrimeMovies)

    @Query("select * from tblCrimeMovies")
    fun getAllData() : LiveData<List<CrimeMovies>>

}