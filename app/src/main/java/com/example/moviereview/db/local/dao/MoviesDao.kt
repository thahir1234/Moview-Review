package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.Movies

@Dao
interface MoviesDao {

    @Upsert
    suspend fun addMovie(movies: Movies)

    @Query("select * from tblMovies where movieId = :movieId")
    fun getParticularMovie(movieId:Int) : LiveData<List<Movies>>

    @Query("select * from tblMovies order by movieName asc")
    fun getAllMoviesNameAsc() : LiveData<List<Movies>>

    @Query("select * from tblMovies order by movieName desc")
    fun getAllMoviesNameDesc() : LiveData<List<Movies>>

    @Query("select * from tblMovies order by rating asc")
    fun getAllMoviesRatingAsc() : LiveData<List<Movies>>

    @Query("select * from tblMovies order by rating desc")
    fun getAllMoviesRatingDesc() : LiveData<List<Movies>>

    @Query("select * from tblMovies")
    fun getAllMovies() : LiveData<List<Movies>>

    @Query("update tblMovies set rating = :newRating , voteCount = :newCount where movieId = :movieId")
    suspend fun updateReview(newRating:Float,newCount:Int,movieId: Int)
}