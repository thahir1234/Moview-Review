package com.example.moviereview.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityMovieBinding
import com.example.moviereview.db.local.entities.*
import com.example.moviereview.db.local.viewmodel.*
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.db.remote.model.*
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.adapter.CastListRecyclerViewAdapter
import com.example.moviereview.view.adapter.GenreRVAdapter
import com.example.moviereview.view.adapter.MovieListRecyclerViewAdapter
import com.example.moviereview.view.adapter.ShortReviewListRecyclerMovieAdapter
import com.example.moviereview.view.fragments.MovieStatusFragment
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.view.list_screen_clean.data.api.dto.MovieList
import com.github.javafaker.Faker
import kotlinx.coroutines.*

class MovieActivity : AppCompatActivity() {

    lateinit var binding: com.example.moviereview.databinding.ActivityMovieBinding


    lateinit var moviesViewModel : MoviesViewModel
    lateinit var similarMoviesViewModel: SimilarMoviesViewModel
    lateinit var castsViewModel: CastsViewModel
    lateinit var favMoviesViewModel: FavMoviesViewModel
    lateinit var reviewsViewModel: ReviewsViewModel
    lateinit var userMoviesViewModel: UserMoviesViewModel
    lateinit var loadedStatusViewModel : LoadedStatusViewModel

    private var globalMovie:LiveData<LongMovieDesc>? = null
    private var similarMovies : LiveData<MovieList>? = null
    private var casts : LiveData<CastList>? = null
    private var reviewsLoaded : LiveData<ReviewList>? =null

    private lateinit var similarAdapter : MovieListRecyclerViewAdapter
    private lateinit var castsAdapter: CastListRecyclerViewAdapter
    private lateinit var reviewAdapter: ShortReviewListRecyclerMovieAdapter
    private lateinit var genreAdapter : GenreRVAdapter

    private lateinit var castsArray : HashSet<Casts>
    private lateinit var reviewsArray : HashSet<Reviews>
    private lateinit var genreArray : HashSet<Genre>

    lateinit var sharedPreferences : SharedPreferences


    private var isLikedMovie = false
    private var movieId = -1
    private var movieName = ""
    private var email = ""
    private var rating = 0f
    private lateinit var statusValues:Array<String>

    private var isLoaded : MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLoaded.value = false
        isLoaded.observe(this)
        {
            if(it)
            {
                binding.movieDetailContent.visibility=View.VISIBLE
                binding.movieDetailPb.visibility = View.GONE
            }
            else
            {
                binding.movieDetailContent.visibility=View.GONE
                binding.movieDetailPb.visibility = View.VISIBLE
            }
        }


        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        similarMoviesViewModel = ViewModelProvider(this).get(SimilarMoviesViewModel::class.java)
        castsViewModel = ViewModelProvider(this).get(CastsViewModel::class.java)
        favMoviesViewModel =ViewModelProvider(this).get(FavMoviesViewModel::class.java)
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        userMoviesViewModel = ViewModelProvider(this).get(UserMoviesViewModel::class.java)
        loadedStatusViewModel = ViewModelProvider(this).get(LoadedStatusViewModel::class.java)
        similarAdapter = MovieListRecyclerViewAdapter(this, loadedStatusViewModel)

        castsAdapter = CastListRecyclerViewAdapter(this)
        reviewAdapter = ShortReviewListRecyclerMovieAdapter(this,this,this)
        genreAdapter = GenreRVAdapter(this)

        movieId = intent.getIntExtra("movieId",-1)
        movieName = intent.getStringExtra("movieName").toString()
        rating = intent.getFloatExtra("rating",0f)
        email = sharedPreferences.getString("email", "").toString()
        statusValues = this.resources.getStringArray(R.array.movie_status)


        lifecycleScope.launch(Dispatchers.IO) {
            globalMovie = APIImplementation.getMovie(movieId)
            withContext(Dispatchers.Main)
            {
                getFullMovieDetails(movieId)
            }
            casts = APIImplementation.getCasts(movieId)
            withContext(Dispatchers.Main)
            {
                getCasts(movieId)
            }
            similarMovies = APIImplementation.getSimilarMovies(movieId)
            withContext(Dispatchers.Main)
            {
                getSimilarMovies(movieId)
            }
            reviewsLoaded = APIImplementation.getReviews(movieId = movieId)
            withContext(Dispatchers.Main)
            {
                loadReviews()
            }
        }


        getReviews()

        setUpCastRv()
        setUpReviewRv()
        setUpSimilarMovRv()
        setUpGenreRv()

        setUpLikeBtn(movieId)
        setUpReviewBtn()


        setUpStartBtn()
        setUpStatusBtn()

        setUpSeeAllReview()

        setUpDescTv()

    }

    private fun setUpStartBtn()
    {
        binding.starBtnMovie.setOnClickListener {
            startActivity(Intent(this,SeeAllReviewActivity::class.java).putExtra("movieId",movieId))
        }
    }
    private fun setUpDescTv()
    {
        binding.descTvMovie.setOnClickListener {
            if(binding.descTvMovie.ellipsize == null)
            {
                binding.descTvMovie.ellipsize = TextUtils.TruncateAt.END
                binding.descTvMovie.maxLines = 3
            }
            else
            {
                binding.descTvMovie.ellipsize = null
                binding.descTvMovie.maxLines = 199
            }
        }
    }
    private fun setUpSeeAllReview()
    {
        binding.reviewSeeallTvMovie.setOnClickListener {
            startActivity(Intent(this,SeeAllReviewActivity::class.java).putExtra("movieId",movieId))
        }
    }
    private fun setUpStatusBtn()
    {
        var position :Int = -1
        userMoviesViewModel.getUserMoviesByBoth(email,movieId)
        userMoviesViewModel.userMoviesByBoth.observe(this)
        {
            Log.i("watch",it.size.toString())
            if(it.isEmpty())
            {
                position =-1
                binding.movieStatusTv.text = "-- : --"
            }
            else{
                position = statusValues.indexOf(it.get(0).Status)
                Log.i("watch",position.toString())
                binding.movieStatusTv.text = it.get(0).Status
                if(position==0)
                {
                    binding.watchBtnMovie.setImageResource(R.drawable.watching_purple)
                }
                else if(position==1)
                {
                    binding.watchBtnMovie.setImageResource(R.drawable.eye_purple)
                }
                else
                {
                    binding.watchBtnMovie.setImageResource(R.drawable.schedule_purple)
                }
            }

        }
        binding.watchBtnMovie.setOnClickListener {
            val statusFragment = MovieStatusFragment(movieId,position)
            statusFragment.show(supportFragmentManager,"")

        }
    }

    private fun getFullMovieDetails(movieId : Int)
    {

        Log.i("searchMovie",movieId.toString())

        globalMovie?.observe(this)
        {
            Log.i("searchMovie",it.name)
            insertMovieToDb(it)
        }

        moviesViewModel.allMovies.observe(this) {
            val currentMovie = moviesViewModel.getParticularMovie(movieId)

            moviesViewModel.partMovie.observe(this)
            {
                Log.i("searchMovie",it.size.toString())
                if(it.size>0) {
                    bindData(it[0])
                }
            }
        }
    }

    private fun insertMovieToDb(movie: LongMovieDesc)
    {
        moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = if (rating==0f) {movie.rating/2} else rating, releaseDate = movie.releaseDate, description = movie.description, voteCount = movie.voteCount, status = movie.status, runtime = movie.runtime))
    }

    private fun bindData(movie: Movies)
    {
        if(movie.movieBanner?.isNotEmpty() == true) {
//            HelperFunction.loadImage(this,"https://image.tmdb.org/t/p/original/" + movie.movieBanner,binding.bannerIvMovie)
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + movie.movieBanner)
                withContext(Dispatchers.Main) {
                    binding.bannerIvMovie.setImageBitmap(bitmap)
                }
            }
        }
        else{
            binding.bannerIvMovie.setImageResource(R.drawable.banner_no)
        }
        if(movie.moviePoster?.isNotEmpty() == true)
        {
            HelperFunction.loadImage(this,"https://image.tmdb.org/t/p/original/" + movie.moviePoster,binding.posterIvMovie)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + movie.moviePoster)
//                withContext(Dispatchers.Main) {
//                    binding.posterIvMovie.setImageBitmap(bitmap)
//                }
//            }
        }
        else
        {
            binding.posterIvMovie.setImageResource(R.drawable.poster_not)
        }
        binding.titleTvMovie.setText(movie.movieName)
        findViewById<TextView>(R.id.actname_tv).text = "Movie"

        binding.descTvMovie.text = movie.description
        if(movie.releaseDate.isNotBlank() && movie.releaseDate.isNotEmpty()) {
            binding.yearTvMovie.setText(movie.releaseDate.substring(0, 4))
        }
        binding.runtimeTvMovie.setText(movie.runtime.toString())
        binding.releasedateTvMovie.setText(movie.releaseDate)
        binding.statusTvMovie.setText(movie.status)

        favMoviesViewModel.getFavMoviesById(movieId)
        favMoviesViewModel.favMoviesById.observe(this){
            binding.likecountTvMovie.text = it.size.toString()
        }
        if(!movie.rating.isNaN()) {
            binding.ratingTvMovie.text = String.format("%.1f", movie.rating).toFloat().toString()
        }
        globalMovie?.observe(this)
        {
            genreArray = hashSetOf()
            for (i in it.genres) {
                genreArray.add(i)
            }

            if (genreArray.isEmpty())
            {
                binding.genreRvMovie.visibility = View.GONE
                binding.noGenres.visibility = View.VISIBLE
            }
            else if(genreArray.size<=3)
            {
                binding.genreRvMovie.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                binding.genreRvMovie.visibility = View.VISIBLE
                binding.noGenres.visibility = View.GONE
            }
            else
            {
                binding.genreRvMovie.visibility = View.VISIBLE
                binding.noGenres.visibility = View.GONE
            }
            genreAdapter.setNewData(genreArray)

        }

    }


    private fun getSimilarMovies(movieId:Int)
    {

        similarMovies?.observe(this)
        {
            if(it.results.size>=5) {
                for (i in 0..5) {
                    insertMovieToDb(it.results.get(i))
                }

                runBlocking {
                    delay(300)

                }
                for (i in 0..5) {
                    similarMoviesViewModel.addSimilarMovie(
                        SimilarMovies(
                            movieId,
                            it.results.get(i).movieId
                        )
                    )
                }
            }

        }

        similarMoviesViewModel.getSimilarMovies(movieId)
        similarMoviesViewModel.similarMovies.observe(this)
        {
            var similarMoviesArray : HashSet<Movies> = hashSetOf()
            if(it.size==0)
            {
                binding.similarmovieRvMovie.visibility = View.GONE
                binding.similarMovieSubstitute.visibility = View.VISIBLE
            }
            else {
                binding.similarmovieRvMovie.visibility = View.VISIBLE
                binding.similarMovieSubstitute.visibility = View.GONE
                for (i in it) {
                    val currentMovie = moviesViewModel.getParticularMovie(i.similarMovieId)
                    moviesViewModel.partMovie.observe(this)
                    {
                        similarMoviesArray.add(it.get(0))
                        similarAdapter.setNewData(similarMoviesArray)

                    }
                }
            }
        }
    }


    private fun getCasts(movieId: Int)
    {

        casts?.observe(this)
        {
            Log.i("casts",it.results.size.toString())
            for(i in 0..(it.results.size))
            {
                if(i<it.results.size) {
                    castsViewModel.addCast(
                        Casts(
                            it.results.get(i).castId,
                            it.movieId,
                            it.results.get(i).castPic,
                            it.results.get(i).name,
                            it.results.get(i).characterName
                        )
                    )
                }
            }

            runBlocking {
                delay(200)
            }
        }

        castsViewModel.getCasts(movieId)
        castsViewModel.casts.observe(this)
        {
            if(it.size==0)
            {
                binding.noCastTv.visibility = View.VISIBLE
                binding.castsRvMovie.visibility=View.GONE
                isLoaded.value = true

            }
            else {
                binding.noCastTv.visibility = View.GONE
                binding.castsRvMovie.visibility=View.VISIBLE
                castsArray = hashSetOf()

                for (i in it.indices) {
                    castsArray.add(it.get(i))
                }
                Log.i("casts", "first time" + it.size)
                isLoaded.value = true

                castsAdapter.setCasts(castsArray)
            }
        }
    }

    private fun loadReviews()
    {
        reviewsLoaded?.observe(this)
        {
            for(i in it.results)
            {
                if(i.authorDetails.userName.isEmpty())
                {
                    i.authorDetails.userName = Faker().name().firstName()
                }
                reviewsViewModel.addReview(Reviews(name = i.authorDetails.userName, movieName = movieName, movieId = movieId, date = i.createdDate.substring(0,10), content = i.content, rating = i.authorDetails.rating/2))
            }
        }
    }
    private fun getReviews()
    {
        reviewsViewModel.getReviewsByMovie(movieId)
        reviewsViewModel.reviewsByMovie.observe(this){

            reviewsArray = hashSetOf()
            if(it.size==0)
            {
                binding.reviewSeeallTvMovie.visibility = View.GONE
                binding.reviewRvMovie.visibility = View.GONE
                binding.reviewSubstituteTv.visibility = View.VISIBLE

            }
            else {
                binding.reviewSeeallTvMovie.visibility = View.VISIBLE
                binding.reviewRvMovie.visibility = View.VISIBLE
                binding.reviewSubstituteTv.visibility = View.GONE
                for (i in it) {
                    reviewsArray.add(i)
                }
            }
            reviewAdapter.setNewReviews(reviewsArray)
        }
    }
    private fun insertMovieToDb(movie: ShortMovieDesc)
    {
        moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
    }

    private fun setUpCastRv()
    {
        binding.castsRvMovie.adapter = castsAdapter
        binding.castsRvMovie.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setUpReviewRv()
    {
        binding.reviewRvMovie.adapter = reviewAdapter
        binding.reviewRvMovie.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setUpSimilarMovRv()
    {
        binding.similarmovieRvMovie.adapter = similarAdapter
        binding.similarmovieRvMovie.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setUpGenreRv()
    {
        binding.genreRvMovie.adapter = genreAdapter
        binding.genreRvMovie.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }
    private fun setUpLikeBtn(movieId: Int)
    {
        favMoviesViewModel.getFavMovies(email!!)
        favMoviesViewModel.favMovies.observe(this)
        {
            for (i in it)
            {
                if(i.movieId == movieId)
                {
                    binding.likeBtnMovie.setImageResource(R.drawable.like_filled)
                    isLikedMovie = true
                }
            }
        }
        binding.likeBtnMovie.setOnClickListener {

            if(!isLikedMovie) {
                val toast = Toast.makeText(this,"Liked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,resources)
                favMoviesViewModel.addFavMovie(FavouriteMovies(email!!, movieId))
                binding.likeBtnMovie.setImageResource(R.drawable.like_filled)

                isLikedMovie=true
            }
            else
            {
                val toast = Toast.makeText(this,"Unliked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,resources)
                favMoviesViewModel.deleteFavMovie(email!!,movieId)
                binding.likeBtnMovie.setImageResource(R.drawable.heart)
                isLikedMovie=false
            }
        }
    }

    private fun setUpReviewBtn()
    {
        reviewsViewModel.getReviewsByBoth(movieId,email)
        reviewsViewModel.reviewsByBoth.observe(this)
        {
            if(it.size>0)
            {
                binding.reviewBtnMovie.setImageResource(R.drawable.rate_review_grey)
                binding.reviewBtnMovie.setOnClickListener {
                    val toast : Toast = Toast.makeText(this,"You already posted a review",Toast.LENGTH_SHORT)
                    HelperFunction.showToast(toast,resources)
                }
            }
            else
            {
                binding.reviewBtnMovie.setOnClickListener {
                    binding.reviewBtnMovie.setImageResource(R.drawable.rate_review_violet)
                    val intent = Intent(this,PostReviewActivity::class.java)
                    intent.putExtra("movieId",movieId)
                    intent.putExtra("movieName",movieName)
                    startActivity(intent)
                }
            }
        }

    }
}