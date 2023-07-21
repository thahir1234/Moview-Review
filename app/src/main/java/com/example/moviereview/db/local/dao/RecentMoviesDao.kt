package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.RecentMovies
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface RecentMoviesDao {

    @Upsert
    suspend fun addRecentMovie(recentMovie: RecentMovies)

    @Query("select * from tblRecentMovies")
    fun getAllData() : LiveData<List<RecentMovies>>
}