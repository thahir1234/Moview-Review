package com.example.moviereview.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.databinding.FragmentFavMovieBinding
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.FavMoviesViewModel
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.view.adapter.FavMoviesRvAdapter

class FavMovieFragment : Fragment() {
    lateinit var binding: FragmentFavMovieBinding

    lateinit var favMoviesViewModel: FavMoviesViewModel
    lateinit var moviesViewModel: MoviesViewModel

    lateinit var sharedPreferences: SharedPreferences

    private var favMoviesArray : ArrayList<Movies>? = arrayListOf()
    private var favMoviesId : HashSet<Int> = hashSetOf()

    private lateinit var favMovieAdapter: FavMoviesRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavMovieBinding.inflate(layoutInflater,container,false)
        binding.favMovieNoDataTv.visibility = View.GONE

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        favMoviesViewModel = ViewModelProvider(this).get(FavMoviesViewModel::class.java)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        favMovieAdapter = FavMoviesRvAdapter(requireContext(),this)

        val email = sharedPreferences.getString("email","")

        if(email!="") {
            favMoviesViewModel.getFavMovies(email!!)
            favMoviesViewModel.favMovies.observe(viewLifecycleOwner)
            {
                if(it.size==0)
                {
                    binding.favMovieRv.visibility = View.GONE
                    binding.favMovieNoDataTv.visibility = View.VISIBLE
                }
                else
                {
                    binding.favMovieRv.visibility = View.VISIBLE
                    binding.favMovieNoDataTv.visibility = View.GONE
                }
                favMoviesId = hashSetOf()
                Log.i("favFrag","before"+it.size.toString())
                for(i in it)
                {
                    favMoviesId.add(i.movieId)
//                    moviesViewModel.getParticularMovie(i.movieId)
//                    moviesViewModel.partMovie.observe(viewLifecycleOwner)
//                    {
//                        Log.i("favFrag",it.get(0).movieName)
//                        favMoviesArray?.add(it.get(0))
//                        favMovieAdapter.setNewFavMovies(favMoviesArray!!)
//                    }
                }
                favMovieAdapter.setNewFavMovies(favMoviesId)
            }
        }
        binding.favMovieRv.adapter = favMovieAdapter
        binding.favMovieRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}