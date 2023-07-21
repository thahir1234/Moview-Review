package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviereview.db.local.entities.UserMovies

@Dao
interface UserMoviesDao {

    @Upsert
    suspend fun addUserMovie(userMovies: UserMovies)

    @Query("select * from tblUserMovies where email = :email and movieId = :movieId")
    fun getUserMoviesByBoth(email:String,movieId:Int) : LiveData<List<UserMovies>>

    @Query("select * from tblUserMovies where email = :email")
    fun getUserMoviesByEmail(email: String) : LiveData<List<UserMovies>>

    @Query("select * from tblUserMovies where movieId = :movieId")
    fun getUserMoviesByMovieId(movieId: Int) : LiveData<List<UserMovies>>
}