package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.AnimationMovies
import com.example.moviereview.db.local.entities.TrendingMovies

@Dao
interface AnimationMoviesDao {

    @Upsert
    suspend fun addAnimationMovie(animationMovie: AnimationMovies)

    @Query("select * from tblAnimationMovies")
    fun getAllData() : LiveData<List<AnimationMovies>>

}