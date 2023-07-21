package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.RomanceMovies
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface RomanceMoviesDao {

    @Upsert
    suspend fun addRomanceMovie(romanceMovie: RomanceMovies)

    @Query("select * from tblRomanceMovies")
    fun getAllData() : LiveData<List<RomanceMovies>>

}