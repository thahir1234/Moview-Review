package com.example.moviereview.view.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivitySeeallReviewBinding
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.ReviewsViewModel
import com.example.moviereview.view.adapter.DepthReviewRvAdapter

class SeeAllReviewActivity : AppCompatActivity() {

    lateinit var binding : ActivitySeeallReviewBinding

    lateinit var reviewsViewModel: ReviewsViewModel

    private var allReviews : HashSet<Reviews> = hashSetOf()

    lateinit var adapter : DepthReviewRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeallReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<TextView>(R.id.actname_tv).text = "All Reviews"
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }

        val movieId : Int = intent.getIntExtra("movieId",-1)

        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)

        adapter = DepthReviewRvAdapter(this,this,this)
        reviewsViewModel.getReviewsByMovie(movieId)
        reviewsViewModel.reviewsByMovie.observe(this)
        {
            for(i in it)
            {
                allReviews.add(i)
                adapter.setNewDate(allReviews)
            }
        }

        binding.depthRv.adapter = adapter
        binding.depthRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }
}