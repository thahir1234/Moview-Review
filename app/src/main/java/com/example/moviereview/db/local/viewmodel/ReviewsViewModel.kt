package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.repositories.ReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewsViewModel(application: Application) : AndroidViewModel(application) {
    var isAsc = true
    var isDsc = false
    var isName = true
    var isRating = false
    var isDate = false

    private val repository : ReviewsRepository

    lateinit var reviewsByMovie : LiveData<List<Reviews>>
    lateinit var reviewsByUser : LiveData<List<Reviews>>
    lateinit var reviewsByBoth : LiveData<List<Reviews>>

    lateinit var allReviews:LiveData<List<Reviews>>
    var allReviewsNameAsc : LiveData<List<Reviews>>
    var allReviewsNameDesc : LiveData<List<Reviews>>
    var allReviewsRatingAsc : LiveData<List<Reviews>>
    var allReviewsRatingDesc : LiveData<List<Reviews>>
    var allReviewsDateAsc : LiveData<List<Reviews>>
    var allReviewsDateDesc : LiveData<List<Reviews>>

    init {
        val reviewsDao = MovieDatabase.getDatabase(application)?.reviewsDao()
        repository = ReviewsRepository(reviewsDao!!)
        allReviews = repository.allReviews
        allReviewsNameAsc = reviewsDao.getReviewsByMovieNameAsc()
        allReviewsNameDesc = reviewsDao.getReviewsByMovieNameDesc()
        allReviewsRatingAsc = reviewsDao.getReviewsByMovieRatingAsc()
        allReviewsRatingDesc = reviewsDao.getReviewsByMovieRatingDesc()
        allReviewsDateAsc = reviewsDao.getReviewsByMovieDateAsc()
        allReviewsDateDesc = reviewsDao.getReviewsByMovieDateDesc()
    }

    fun addReview(review : Reviews)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReview(review)
        }
    }

    fun getReviewsByMovie(movieId:Int)
    {
        reviewsByMovie = repository.getReviewsByMovie(movieId)
    }

    fun getReviewsByUser(email:String)
    {
        reviewsByUser = repository.getReviewsByUser(email)
    }

    fun getReviewsByBoth(movieId: Int,email: String)
    {
        reviewsByBoth = repository.getReviewsByBoth(movieId,email)
    }

    fun deleteReview(movieId: Int,email: String)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.deleteReview(movieId,email)
        }
    }
}