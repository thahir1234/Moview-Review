package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moviereview.db.local.entities.LikedReviews

@Dao
interface LikedReviewsDao {

    @Insert
    suspend fun addLikedReview(likedReview:LikedReviews)

    @Query("delete from tblLikedReviews where email = :email and reviewId = :reviewId")
    suspend fun deleteLikedReview(email:String,reviewId:Int)

    @Query("select * from tblLikedReviews where reviewId = :reviewId")
    fun getCountByReviewId(reviewId: Int) : LiveData<List<LikedReviews>>
}