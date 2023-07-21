package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.Reviews

@Dao
interface ReviewsDao {

    @Upsert
    suspend fun addReview(review: Reviews)

    @Query("select * from tblReviews")
    fun getAllReviews() : LiveData<List<Reviews>>

    @Query("select * from tblReviews where movieId = :movieId")
    fun getReviewsByMovie(movieId:Int) : LiveData<List<Reviews>>

    @Query("select * from tblReviews where email = :email")
    fun getReviewsByUser(email:String) : LiveData<List<Reviews>>

    @Query("delete from tblReviews where email = :email and movieId = :movieId")
    suspend fun deleteReview(movieId: Int,email: String)

    @Query("select * from tblReviews where email = :email and movieId = :movieId")
    fun getReviewsByBoth(movieId: Int,email: String) : LiveData<List<Reviews>>

    //sorting

    @Query("select * from tblReviews order by movieName asc")
    fun getReviewsByMovieNameAsc() : LiveData<List<Reviews>>

    @Query("select * from tblReviews order by movieName desc")
    fun getReviewsByMovieNameDesc() : LiveData<List<Reviews>>

    @Query("select * from tblReviews order by rating asc")
    fun getReviewsByMovieRatingAsc() : LiveData<List<Reviews>>

    @Query("select * from tblReviews order by rating desc")
    fun getReviewsByMovieRatingDesc() : LiveData<List<Reviews>>

    @Query("select * from tblReviews order by date(date) asc")
    fun getReviewsByMovieDateAsc() : LiveData<List<Reviews>>

    @Query("select * from tblReviews order by date(date) desc")
    fun getReviewsByMovieDateDesc() : LiveData<List<Reviews>>
}