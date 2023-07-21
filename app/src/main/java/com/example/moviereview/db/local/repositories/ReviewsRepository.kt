package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.ReviewsDao
import com.example.moviereview.db.local.entities.Reviews

class ReviewsRepository(private val reviewsDao: ReviewsDao) {

    lateinit var reviewsByMovie : LiveData<List<Reviews>>

    lateinit var reviewsByUser : LiveData<List<Reviews>>

    lateinit var reviewsByBoth : LiveData<List<Reviews>>

    var allReviews = reviewsDao.getAllReviews()

    suspend fun addReview(review: Reviews)
    {
        reviewsDao.addReview(review)
    }

    fun getReviewsByMovie(movieId:Int) : LiveData<List<Reviews>>
    {
        reviewsByMovie = reviewsDao.getReviewsByMovie(movieId)
        return reviewsByMovie
    }

    fun getReviewsByUser(email:String) : LiveData<List<Reviews>>
    {
        reviewsByUser = reviewsDao.getReviewsByUser(email)
        return reviewsByUser
    }

    fun getReviewsByBoth(movieId: Int,email: String) : LiveData<List<Reviews>>
    {
        reviewsByBoth = reviewsDao.getReviewsByBoth(movieId,email)
        return reviewsByBoth
    }
    suspend fun deleteReview(movieId: Int,email: String)
    {
        reviewsDao.deleteReview(movieId,email)
    }
}