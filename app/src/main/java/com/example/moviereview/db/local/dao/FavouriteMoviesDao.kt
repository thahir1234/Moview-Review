package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.FavouriteMovies
import com.example.moviereview.db.local.entities.Movies

@Dao
interface FavouriteMoviesDao {

    @Upsert
    suspend fun addFavMovie(movie: FavouriteMovies)

    @Query("select * from tblFavouriteMovies where email = :email")
    fun getFavMovies(email:String) : LiveData<List<FavouriteMovies>>

    @Query("select * from tblFavouriteMovies where movieId = :movieId")
    fun getFavMoviesById(movieId: Int) : LiveData<List<FavouriteMovies>>

    @Query("delete from tblFavouriteMovies where email = :email and movieId = :movieId")
    suspend fun deleteFavMovie(email: String,movieId:Int)
}