package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.LoadedStatus

@Dao
interface LoadedStatusDao {

    @Upsert
    suspend fun addLoadedStatus(loadedStatus: LoadedStatus)

    @Query("delete from tblLoadedStatus")
    suspend fun deleteAllData()

    @Query("select * from tblLoadedStatus")
    fun getAllData() : LiveData<List<LoadedStatus>>

    @Query("update tblLoadedStatus set popularMovies = 1")
    suspend fun updatePopular()

    @Query("update tblLoadedStatus set trendingMovies = 1")
    suspend fun updateTrending()
}