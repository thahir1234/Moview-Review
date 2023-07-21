package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.ActionMovies

@Dao
interface ActionMoviesDao {

    @Upsert
    suspend fun addActionMovies(actionMovie: ActionMovies)

    @Query("select * from tblActionMovies")
    fun getAllData() : LiveData<List<ActionMovies>>

}