package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.LikedReviews
import com.example.moviereview.db.local.repositories.LikedReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikedReviewsViewModel (application: Application) : AndroidViewModel(application) {
    private val repository : LikedReviewRepository

    lateinit var countByReviews : LiveData<List<LikedReviews>>
    init {
        val likedReviewsDao = MovieDatabase.getDatabase(application)?.likedReviewsDao()
        repository = LikedReviewRepository(likedReviewsDao!!)

    }

    fun addLikedReview(likedReview: LikedReviews)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLikedReview(likedReview)
        }
    }

    fun deleteLikedReview(email:String,reviewId:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLikedReview(email,reviewId)
        }
    }

    fun getCountByReviews(reviewId: Int)
    {
        countByReviews = repository.getCountByReviewId(reviewId)
    }
}