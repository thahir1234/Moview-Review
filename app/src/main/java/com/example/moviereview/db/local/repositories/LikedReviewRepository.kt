package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.LikedReviewsDao
import com.example.moviereview.db.local.entities.LikedReviews

class LikedReviewRepository (private val likedReviewsDao: LikedReviewsDao) {

    lateinit var countByReviewId : LiveData<List<LikedReviews>>
    suspend fun addLikedReview(likedReview: LikedReviews)
    {
        likedReviewsDao.addLikedReview(likedReview)
    }

    suspend fun deleteLikedReview(email:String,reviewId:Int)
    {
        likedReviewsDao.deleteLikedReview(email,reviewId)
    }

    fun getCountByReviewId(reviewId: Int) : LiveData<List<LikedReviews>>
    {
        countByReviewId = likedReviewsDao.getCountByReviewId(reviewId)
        return countByReviewId
    }
}