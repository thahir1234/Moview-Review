package com.example.moviereview.view.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityPostReviewBinding
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.db.local.viewmodel.ReviewsViewModel
import com.example.moviereview.utils.HelperFunction
import java.text.SimpleDateFormat
import java.util.*

class PostReviewActivity : AppCompatActivity() {

    lateinit var binding:ActivityPostReviewBinding

    lateinit var reviewsViewModel: ReviewsViewModel
    lateinit var moviesViewModel: MoviesViewModel

    lateinit var sharedPreferences : SharedPreferences

    private var movieId :Int = -1
    private var movieName : String = ""
    private var email : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        movieId =intent.getIntExtra("movieId",-1)
        movieName = intent.getStringExtra("movieName").toString()
        email = sharedPreferences.getString("email", "").toString()

        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<TextView>(R.id.actname_tv).text = "Post Review"
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }
        binding.ratingValueTv.visibility = View.GONE


        binding.reviewRb.setOnRatingBarChangeListener(object:RatingBar.OnRatingBarChangeListener{
            override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
                val value = binding.reviewRb.rating

                binding.ratingValueTv.apply {
                    if(value == 1.0f || value == 2.0f || value == 3.0f||value == 4.0f ||value == 5.0f)
                    {
                        setText("Rating : " + value.toInt().toString())
                    }
                    else {
                        setText("Rating : " + value.toString())
                    }
                    visibility = View.VISIBLE
                }

            }

        })


        val cond:MutableLiveData<Boolean> = checkNewReview()

        cond.observe(this)
        {
            if(it)
            {
                findViewById<ImageView>(R.id.tick_iv).setOnClickListener {
                    var previousRating : Float
                    var previousVoteCount : Int = -1
                    var newRating : Float = -1f

                    val name: String? = sharedPreferences.getString("name", "")

                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val date = Date()
                    val current = formatter.format(date)

                    val content  =  binding.reviewEt.text.toString()
                    val rating  = binding.reviewRb.rating

                    if(rating>=0.5) {
                        moviesViewModel.getParticularMovie(movieId)
                        moviesViewModel.partMovie.observe(this)
                        {
                            Log.i("review", "calculating")
                            previousRating = it.get(0).rating
                            previousVoteCount = it.get(0).voteCount
                            newRating =
                                ((previousRating * previousVoteCount) + rating) / (previousVoteCount + 1)
                            moviesViewModel.updateReview(newRating, previousVoteCount + 1, movieId)

                        }

                        Log.i("review", "Email:" + email)
                        Log.i("review", "MovieID:" + movieId)
                        Log.i("review", "content:" + content)

                        reviewsViewModel.addReview(
                            Reviews(
                                email!!,
                                name!!,
                                movieName,
                                movieId,
                                current,
                                content.trim(),
                                rating
                            )
                        )
                        val toast = Toast.makeText(this,"Posted",Toast.LENGTH_SHORT)
                        HelperFunction.showToast(toast,resources)
                        finish()
                    }
                    else
                    {
                        val toast = Toast.makeText(this,"Give at least a rating of 0.5",Toast.LENGTH_SHORT)
                        HelperFunction.showToast(toast,resources)
                    }
                }
            }
            else{
                findViewById<ImageView>(R.id.tick_iv).setOnClickListener {
                    Toast.makeText(this,"You already posted a review",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun checkNewReview() : MutableLiveData<Boolean>
    {
        val res:MutableLiveData<Boolean> = MutableLiveData()
        reviewsViewModel.getReviewsByBoth(movieId,email)
        reviewsViewModel.reviewsByBoth.observe(this)
        {
            res.value = it.size <= 0
        }
        return res
    }

    override fun onResume() {
        super.onResume()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.GONE
    }
}