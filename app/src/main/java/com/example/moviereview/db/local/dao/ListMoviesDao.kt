package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Movies

@Dao
interface ListMoviesDao {

    @Upsert
    suspend fun addListMovie(listMovie:ListMovies)

    @Query("select * from tblListMovies where listId = :listId")
    fun getAllListMovies(listId:Int) : LiveData<List<ListMovies>>

    @Query("select * from tblListMovies where movieId = :movieId")
    fun getAllMoviesById(movieId: Int) : LiveData<List<ListMovies>>

    @Query("select * from tblListMovies where listId = :listId and movieId = :movieId")
    fun getMoviesByBoth(listId: Int,movieId: Int) : LiveData<List<ListMovies>>

    @Query("delete from tblListMovies where movieId = :movieId and listId = :listId")
    suspend fun deleteListMovie(movieId:Int,listId:Int)
}