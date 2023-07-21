package com.example.moviereview.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.moviereview.R
import com.example.moviereview.databinding.FragmentHomeBinding
import com.example.moviereview.db.local.entities.*
import com.example.moviereview.db.local.viewmodel.*
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.db.remote.model.MovieList
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.view.activities.DefaultActivity
import com.example.moviereview.view.activities.SeeAllMovieActivity
import com.example.moviereview.view.adapter.BannerViewPagerAdapter
import com.example.moviereview.view.adapter.MovieListRecyclerViewAdapter
import kotlinx.coroutines.*
import java.lang.Math.abs
import java.lang.Runnable


class HomeFragment : Fragment() {


    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var adapter:BannerViewPagerAdapter


    //private var popularMoviesArray : HashSet<Movies> = hashSetOf()
    //private var trendingMoviesArray : HashSet<Movies> = hashSetOf()
    //private var recentMovieArray : HashSet<Movies> = hashSetOf()

    private lateinit var popularAdapter : BannerViewPagerAdapter
    private lateinit var trendingAdapter : MovieListRecyclerViewAdapter
    private lateinit var recentAdapter : MovieListRecyclerViewAdapter
    private lateinit var actionAdapter : MovieListRecyclerViewAdapter
    private lateinit var adventureAdapter : MovieListRecyclerViewAdapter
    private lateinit var animationAdapter : MovieListRecyclerViewAdapter
    private lateinit var romanceAdapter : MovieListRecyclerViewAdapter
    private lateinit var crimeAdapter : MovieListRecyclerViewAdapter


    private var _binding: FragmentHomeBinding? = null

    private var popularMovies : LiveData<MovieList>? =null
    private var recentMovies : LiveData<MovieList>? = null
    private var trendingMovies : LiveData<MovieList>? = null
    private var actionMovies : LiveData<MovieList>? = null
    private var adventureMovies : LiveData<MovieList>? = null
    private var animationMovies : LiveData<MovieList>? = null
    private var romanceMovies : LiveData<MovieList>? = null
    private var crimeMovies : LiveData<MovieList>? = null


    private val binding get() = _binding!!

    lateinit var moviesViewModel: MoviesViewModel
    lateinit var popularMoviesViewModel: PopularMoviesViewModel
    lateinit var trendingMovieViewModel: TrendingMovieViewModel
    lateinit var recentMovieViewModel: RecentMovieViewModel
    lateinit var actionMoviesViewModel: ActionMoviesViewModel
    lateinit var adventureMoviesViewModel: AdventureMoviesViewModel
    lateinit var animationMoviesViewModel: AnimationMoviesViewModel
    lateinit var romanceMoviesViewModel: RomanceMoviesViewModel
    lateinit var crimeMoviesViewModel: CrimeMoviesViewModel
    lateinit var loadedStatusViewModel: LoadedStatusViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        Log.i("lifecycle","Home fragment Created")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val act = activity as DefaultActivity
        act.setSupportActionBar(binding.navToolbar)

        val drawer = act.findViewById<DrawerLayout>(R.id.drawer_layout)

        val actionBarToggle : ActionBarDrawerToggle = ActionBarDrawerToggle(act,drawer,binding.navToolbar,R.string.drawer_open,R.string.drawer_close)

        drawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        popularMoviesViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
        trendingMovieViewModel = ViewModelProvider(this).get(TrendingMovieViewModel::class.java)
        recentMovieViewModel = ViewModelProvider(this).get(RecentMovieViewModel::class.java)
        actionMoviesViewModel = ViewModelProvider(this).get(ActionMoviesViewModel::class.java)
        adventureMoviesViewModel = ViewModelProvider(this).get(AdventureMoviesViewModel::class.java)
        animationMoviesViewModel = ViewModelProvider(this).get(AnimationMoviesViewModel::class.java)
        romanceMoviesViewModel = ViewModelProvider(this).get(RomanceMoviesViewModel::class.java)
        crimeMoviesViewModel = ViewModelProvider(this).get(CrimeMoviesViewModel::class.java)
        loadedStatusViewModel = ViewModelProvider(this).get(LoadedStatusViewModel::class.java)


        trendingAdapter = MovieListRecyclerViewAdapter(requireContext(),loadedStatusViewModel)
        recentAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)
        actionAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)
        adventureAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)
        animationAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)
        romanceAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)
        crimeAdapter = MovieListRecyclerViewAdapter(requireContext(), loadedStatusViewModel)

        popularAdapter = BannerViewPagerAdapter(requireContext(),loadedStatusViewModel)


        binding.homeProgressBar.visibility = View.VISIBLE
        binding.homeContent.visibility = View.INVISIBLE


        loadedStatusViewModel.addLoadedStatus(LoadedStatus(0,0,0,0,0,0,0,0,0))

//        val job2 = lifecycleScope.launch(Dispatchers.IO) {
//        }
//
//        runBlocking {
//            job1.join()
//        }
//        runBlocking {
//            job2.join()
//
//        }

        loadedStatusViewModel.allData.observe(viewLifecycleOwner)
        {
            if(!it.isEmpty())
            {
                if(it.get(0).popularMovies == 1 && it.get(0).trendingMovies==1)
                {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.homeContent.visibility = View.VISIBLE
                }
            }
        }
        apiCalls()

        seeAllListeners()


        return binding.root
    }


    private fun apiCalls(): MovieList? {


        lifecycleScope.launch(Dispatchers.IO) {
            popularMovies = APIImplementation.getPopularMovies()
            withContext(Dispatchers.Main)
            {
                popularMoviesObserver()
            }
            trendingMovies = APIImplementation.getTrendingMovies()
            withContext(Dispatchers.Main)
            {
                trendingMoviesObserver()
            }
            recentMovies = APIImplementation.getRecentMovies()
            withContext(Dispatchers.Main)
            {
                recentMovieObserver()
            }
            actionMovies = APIImplementation.getMoviesByGenre(genre = "28")
            withContext(Dispatchers.Main)
            {
                actionMovieObserver()
            }
            adventureMovies = APIImplementation.getMoviesByGenre(genre = "12")
            withContext(Dispatchers.Main)
            {
                adventureMovieObserver()
            }
            animationMovies = APIImplementation.getMoviesByGenre(genre = "16")
            withContext(Dispatchers.Main)
            {
                animationMovieObserver()
            }
            romanceMovies = APIImplementation.getMoviesByGenre(genre = "10749")
            withContext(Dispatchers.Main)
            {
                romanceMovieObserver()
            }
            crimeMovies = APIImplementation.getMoviesByGenre(genre = "80")
            withContext(Dispatchers.Main)
            {
                crimeMovieObserver()
            }
        }

        return popularMovies?.value
    }


    private fun popularMoviesObserver()
    {
        popularMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }


            for(i in 0..5)
            {
                popularMoviesViewModel.addPopularMovie(PopularMovies( it.results.get(i).movieId))
            }
        }

        popularMoviesViewModel.allPopularMovies.observe(viewLifecycleOwner)
        {
            var popularMoviesArray : HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    popularMoviesArray.add(it.get(0))
                    adapter.setNewData(popularMoviesArray)

                }
            }
        }
    }
    private fun trendingMoviesObserver()
    {
        trendingMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }


            for (i in 0..5)
            {
                trendingMovieViewModel.addTrendingMovie(TrendingMovies( it.results.get(i).movieId))
            }
        }

        trendingMovieViewModel.allTrendingMovies.observe(viewLifecycleOwner)
        {
            var trendingMoviesArray : HashSet<Movies> = hashSetOf()
            //var count = 0
            //Log.i("trendingValues","\n\n"+it.size)
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    //Log.i("trendingValues",count++.toString() + it.get(0).movieName)
                    trendingMoviesArray.add(it.get(0))
                    trendingAdapter.setNewData(trendingMoviesArray)
                }
            }
            var count = 0
            for(i in trendingMoviesArray) {
                Log.i("trendingValues",count.toString()+" "+i.movieName)
                count++
            }
        }
    }

    private fun recentMovieObserver()
    {
        recentMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                recentMovieViewModel.addRecentMovie(RecentMovies(it.results.get(i).movieId))
            }
        }


        recentMovieViewModel.allRecentMovies.observe(viewLifecycleOwner)
        {
            var recentMovieArray: HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    recentMovieArray.add(it.get(0))
                    recentAdapter.setNewData(recentMovieArray)

                }
            }
        }
    }

    private fun actionMovieObserver()
    {
        actionMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                actionMoviesViewModel.addActionMovie(ActionMovies(it.results.get(i).movieId))
            }
        }


        actionMoviesViewModel.allActionMovies.observe(viewLifecycleOwner)
        {
            var actionMovieArray: HashSet<Movies> = hashSetOf()
            var count = 0
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    actionMovieArray.add(it.get(0))
                    actionAdapter.setNewData(actionMovieArray)
                }
            }
        }
    }

    private fun adventureMovieObserver()
    {
        adventureMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                adventureMoviesViewModel.addAdventureMovie(AdventureMovies(it.results.get(i).movieId))
            }
        }


        adventureMoviesViewModel.allAdventureMovies.observe(viewLifecycleOwner)
        {
            var advetureMovieArray: HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    advetureMovieArray.add(it.get(0))
                    adventureAdapter.setNewData(advetureMovieArray)
                }
            }
        }
    }

    private fun animationMovieObserver()
    {
        animationMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                animationMoviesViewModel.addAnimationMovie(AnimationMovies(it.results.get(i).movieId))
            }
        }


        animationMoviesViewModel.allAnimationMovies.observe(viewLifecycleOwner)
        {
            var animationMovieArray: HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    animationMovieArray.add(it.get(0))
                    animationAdapter.setNewData(animationMovieArray)

                }
            }
        }
    }

    private fun romanceMovieObserver()
    {
        romanceMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                romanceMoviesViewModel.addRomanceMovie(RomanceMovies(it.results.get(i).movieId))
            }
        }


        romanceMoviesViewModel.allRomanceMovies.observe(viewLifecycleOwner)
        {
            var romanceMovieArray: HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    romanceMovieArray.add(it.get(0))
                    romanceAdapter.setNewData(romanceMovieArray)
                }
            }
        }
    }

    private fun crimeMovieObserver()
    {
        crimeMovies?.observe(viewLifecycleOwner){
            for(i in 0..5){
                val movie = it.results.get(i)
                moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))
            }

            for(i in 0..5)
            {
                crimeMoviesViewModel.addCrimeMovie(CrimeMovies(it.results.get(i).movieId))
            }
        }


        crimeMoviesViewModel.allCrimeMovies.observe(viewLifecycleOwner)
        {
            var crimeMovieArray: HashSet<Movies> = hashSetOf()
            for(i in it)
            {
                val currentMovie = moviesViewModel.getParticularMovie(i.movieId)
                moviesViewModel.partMovie.observe(viewLifecycleOwner)
                {
                    crimeMovieArray.add(it.get(0))
                    crimeAdapter.setNewData(crimeMovieArray)

                }
            }
        }
    }

    private fun seeAllListeners()
    {
        binding.trendingSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","trending")
            }
            startActivity(intent)
        }

        binding.recentSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","recent")
            }
            startActivity(intent)
        }
        binding.actionSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","action")
            }
            startActivity(intent)
        }
        binding.adventureSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","adventure")
            }
            startActivity(intent)
        }
        binding.animationSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","animation")
            }
            startActivity(intent)
        }
        binding.romanceSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","romance")
            }
            startActivity(intent)
        }
        binding.crimeSeeallTv.setOnClickListener {
            val intent = Intent(requireContext(),SeeAllMovieActivity::class.java).apply {
                putExtra("from","crime")
            }
            startActivity(intent)
        }
    }
    private fun insertMovieToDb(movie:ShortMovieDesc)
    {
        moviesViewModel.addMovie(Movies(movieId = movie.movieId, movieBanner = movie.banner, moviePoster = movie.poster, movieName = movie.name, rating = movie.rating/2, releaseDate = movie.releaseDate, description = "", voteCount = movie.voteCount, status = "", runtime = 234))

    }

    override fun onPause() {
        super.onPause()

        Log.i("lifecycle","Home fragment Paused")
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        intializeBannerViewPager()

        intializeMovieListRV()
        Log.i("lifecycle","Home fragment Resumed")

        handler.postDelayed(runnable , 2000)
    }

    //Banner view pager methods(intializeBannerViewPager(),setUpTransformer(),runnable)
    private fun intializeBannerViewPager()
    {
        viewPager2= binding.bannerVp
        handler = Handler(Looper.myLooper()!!)


        adapter = popularAdapter
        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("lifecycle","Home Fragment Destroyed")
    }
    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    //Movie List (Trending) Recyclerview
    private fun intializeMovieListRV()
    {

        var trendingRecyclerView = _binding!!.trendingRv

        var recentRecyclerView = _binding!!.recentRv
        var actionRecyclerView = _binding!!.actionRv
        var adventureRecyclerView = binding.adventureRv
        var animationRecyclerView = binding.animationRv
        var romanceRecyclerView = binding.romanceRv
        var crimeRecyclerView = binding.crimeRv


        trendingRecyclerView.adapter = trendingAdapter
        trendingRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        recentRecyclerView.adapter = recentAdapter
        recentRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        actionRecyclerView.adapter = actionAdapter
        actionRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        adventureRecyclerView.adapter = adventureAdapter
        adventureRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        animationRecyclerView.adapter = animationAdapter
        animationRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        romanceRecyclerView.adapter = romanceAdapter
        romanceRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        crimeRecyclerView.adapter = crimeAdapter
        crimeRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

    }

}