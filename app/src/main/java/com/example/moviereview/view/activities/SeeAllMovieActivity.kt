package com.example.moviereview.view.activities

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityMovielistBinding
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.db.local.viewmodel.PopularMoviesViewModel
import com.example.moviereview.db.local.viewmodel.RecentMovieViewModel
import com.example.moviereview.db.local.viewmodel.TrendingMovieViewModel
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.view.list_screen_clean.data.api.dto.MovieList
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.view.adapter.SearchResultRvAdapter

class SeeAllMovieActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovielistBinding

    private var movies : ArrayList<ShortMovieDesc> = arrayListOf()
    private var movies2 : HashSet<String> = HashSet()

    lateinit var moviesViewModel: MoviesViewModel
    lateinit var popularMoviesViewModel: PopularMoviesViewModel
    lateinit var trendingMovieViewModel: TrendingMovieViewModel
    lateinit var recentMovieViewModel: RecentMovieViewModel

    private var results : LiveData<MovieList>? =null

    lateinit var adapter: SearchResultRvAdapter

    private var page:Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovielistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noMoviesTv.visibility = View.GONE
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }
        //addGapBetween()
        val seeAllRv = binding.movielistRv
        val category = intent.getStringExtra("from")

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        popularMoviesViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
        trendingMovieViewModel = ViewModelProvider(this).get(TrendingMovieViewModel::class.java)
        recentMovieViewModel = ViewModelProvider(this).get(RecentMovieViewModel::class.java)

        //adapter = MovieListSeeAllRvAdapter(this,this)
        adapter = SearchResultRvAdapter(this)
        binding.seeallNsv.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY== v.getChildAt(0).measuredHeight - v.measuredHeight)
                {
                    page++
                    decideToLoad(category!!)
                }
            }
        })

        decideToLoad(category!!)
        seeAllRv.adapter = adapter
        seeAllRv.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

    }


    private fun decideToLoad(category: String)
    {
        when(category)
        {
            "trending"->{
                setUpTrendingMovies(page.toString())
            }
            "recent"->{
                setUpRecentMovies(page.toString())
            }
            "action"->{
                setUpActionMovies(page.toString())
            }
            "adventure"->{
                setUpAdventureMovies(page.toString())
            }
            "animation"->{
                setUpAnimationMovies(page.toString())
            }
            "romance"->{
                setUpRomanceMovies(page.toString())
            }
            "crime"->{
                setUpCrimeMovies(page.toString())
            }
        }
    }


    private fun addGapBetween()
    {
        val spacing = 30
        binding.movielistRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

                // Add spacing between items based on their span index
                if (spanIndex == 0) {
                    // For items in the left column, add spacing to the right
                    outRect.right = spacing
                } else {
                    // For items in the right column, add spacing to the left
                    outRect.left = spacing
                }

                // Add spacing at the top and bottom of each item
                outRect.top = spacing
                outRect.bottom = spacing
            }
        })
    }

    private fun setUpTrendingMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Trending")
        results = APIImplementation.getTrendingMovies(page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }

    private fun setUpRecentMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Recent")
        results = APIImplementation.getRecentMovies(page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }

    private fun setUpActionMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Action")
        results = APIImplementation.getMoviesByGenre(genre= "28",page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }
    private fun setUpAdventureMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Recent")
        results = APIImplementation.getMoviesByGenre(genre="12",page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }
    private fun setUpAnimationMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Animation")
        results = APIImplementation.getMoviesByGenre(genre="16",page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }
    private fun setUpRomanceMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Romance")
        results = APIImplementation.getMoviesByGenre(genre="10749",page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }
    private fun setUpCrimeMovies(page:String) {
        findViewById<TextView>(R.id.actname_tv).setText("Crime")
        results = APIImplementation.getMoviesByGenre(genre="80",page = page)

        results?.observe(this)
        {
            for (i in 0 until it.results.size) {
                movies.add(it.results.get(i))
            }
            adapter.setNewResults(movies)
            //Log.i("search", it.results.size.toString())
        }
    }
}


