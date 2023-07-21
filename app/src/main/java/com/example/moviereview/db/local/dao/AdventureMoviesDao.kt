package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.AdventureMovies
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface AdventureMoviesDao {

    @Upsert
    suspend fun addAdventureMovie(adventureMovie: AdventureMovies)

    @Query("select * from tblAdventureMovies")
    fun getAllData() : LiveData<List<AdventureMovies>>

}