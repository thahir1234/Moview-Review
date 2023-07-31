package com.example.moviereview.view.activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityMovielistBinding
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.db.local.viewmodel.UserMoviesViewModel
import com.example.moviereview.view.adapter.SearchResultRvAdapter
import com.google.android.material.navigation.NavigationView

class WatchingActivity : AppCompatActivity() {

    lateinit var binding : ActivityMovielistBinding
    lateinit var userMoviesViewModel: UserMoviesViewModel
    lateinit var moviesViewModel: MoviesViewModel

    lateinit var sharedPreferences : SharedPreferences

    private var email = ""
    private var movies : ArrayList<Movies> = arrayListOf()
    private var factor = ""

    lateinit var adapter: SearchResultRvAdapter

    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovielistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        email = sharedPreferences.getString("email", "").toString()

        userMoviesViewModel = ViewModelProvider(this).get(UserMoviesViewModel::class.java)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        factor = intent.getStringExtra("key").toString()
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<TextView>(R.id.actname_tv).text = factor
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }
        binding.progessBar.visibility = View.GONE

        adapter = SearchResultRvAdapter(this)
        binding.movielistRv.adapter = adapter
        binding.movielistRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_watching);

        processSorting(dialog)

        findViewById<ImageView>(R.id.sort_iv).setOnClickListener {
            showBottomDialog()
        }
        //addGapBetween()
    }

    private fun showBottomDialog()
    {


        dialog.findViewById<ImageView>(R.id.seeall_sort_cut_iv).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.seeall_sort_tick_iv).setOnClickListener {
            val isAsc = dialog.findViewById<RadioButton>(R.id.asc_rb)
            val isDesc = dialog.findViewById<RadioButton>(R.id.desc_rb)

            val isName = dialog.findViewById<RadioButton>(R.id.name_rb)
            val isRating = dialog.findViewById<RadioButton>(R.id.rating_rb)
            val isDate = dialog.findViewById<RadioButton>(R.id.date_rb)
            if(isAsc.isChecked)
            {
                if(isName.isChecked)
                {
                    userMoviesViewModel.isAsc = true
                    userMoviesViewModel.isDsc = false
                    userMoviesViewModel.isName = true
                    userMoviesViewModel.isRating = false
                }
                else if(isRating.isChecked)
                {
                    userMoviesViewModel.isAsc = true
                    userMoviesViewModel.isDsc = false
                    userMoviesViewModel.isName = false
                    userMoviesViewModel.isRating = true

                }
                dialog.dismiss()
            }
            else if(isDesc.isChecked)
            {
                if(isName.isChecked)
                {
                    userMoviesViewModel.isAsc = false
                    userMoviesViewModel.isDsc = true
                    userMoviesViewModel.isName = true
                    userMoviesViewModel.isRating = false
                }
                else if(isRating.isChecked)
                {
                    userMoviesViewModel.isAsc = false
                    userMoviesViewModel.isDsc = true
                    userMoviesViewModel.isName = false
                    userMoviesViewModel.isRating = true

                }
                dialog.dismiss()
            }
            processSorting(dialog)
        }
        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
    }

    private fun processSorting(dialog: Dialog)
    {
        var ascRb = dialog.findViewById<RadioButton>(R.id.asc_rb)
        var nameRb = dialog.findViewById<RadioButton>(R.id.name_rb)
        var descRb = dialog.findViewById<RadioButton>(R.id.desc_rb)
        var ratingRb = dialog.findViewById<RadioButton>(R.id.rating_rb)

        if(userMoviesViewModel.isAsc && userMoviesViewModel.isName)
        {
            ascRb.isChecked = true
            nameRb.isChecked = true
            moviesViewModel.allMoviesNameAsc.observe(this)
            {
                setUpRv(it)
            }
        }
        else if(userMoviesViewModel.isAsc && userMoviesViewModel.isRating){
            ascRb.isChecked = true
            ratingRb.isChecked =true
            moviesViewModel.allMoviesRatingAsc.observe(this)
            {
                setUpRv(it)
            }
        }
        else if(userMoviesViewModel.isDsc && userMoviesViewModel.isName)
        {
            descRb.isChecked = true
            nameRb.isChecked =true
            moviesViewModel.allMoviesNameDesc.observe(this)
            {
                setUpRv(it)
            }
        }
        else
        {
            descRb.isChecked =true
            ratingRb.isChecked =true
            moviesViewModel.allMoviesRatingDesc.observe(this)
            {
                setUpRv(it)
            }
        }
    }
    private fun setUpRv(allMovies:List<Movies>)
    {
        movies = arrayListOf()
        userMoviesViewModel.getUserMoviesByEmail(email)
        userMoviesViewModel.userMoviesByEmail.observe(this)
        {
            for(i in allMovies)
            {
                for(j in it)
                {
                    if((i.movieId == j.movieId) && (j.Status == factor))
                    {
                        movies.add(i)
                    }
                }
            }
            if(movies.size>0)
            {
                binding.noMoviesTv.visibility = View.GONE
            }
            else
            {
                binding.noMoviesTv.visibility = View.VISIBLE
            }
            adapter.setNewResultsMovies(movies)
//            Log.i("usermovies",it.size.toString())
//            for(i in it)
//            {
//                if(i.Status == factor)
//                {
//                    val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
//                    moviesViewModel.partMovie.observe(this)
//                    {
//                        movies.add(it.get(0))
//                        Log.i("usermovies",movies.size.toString())
//                        adapter.setNewResultsMovies(movies)
//                    }
//
//                }
//
//            }
        }

    }
    override fun onBackPressed() {
        setResult(100, Intent())
        finish()
    }
}