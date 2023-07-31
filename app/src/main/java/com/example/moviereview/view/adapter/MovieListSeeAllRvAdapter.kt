package com.example.moviereview.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.SeeAllMovieActivity
import java.util.LinkedList
import kotlinx.coroutines.*

class MovieListSeeAllRvAdapter(var context: Context, var yourReviewFragment: SeeAllMovieActivity):RecyclerView.Adapter<MovieListSeeAllRvAdapter.MyViewHolder>() {

    lateinit var moviesViewModel: MoviesViewModel
    private var movies : List<Movies> = LinkedList()

    class MyViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var poster = itemView.findViewById<ImageView>(R.id.poster_iv_movielist)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_movielist,parent,false)

        moviesViewModel = ViewModelProvider(yourReviewFragment).get(MoviesViewModel::class.java)
        return MovieListSeeAllRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = movies[position]

        //moviesViewModel.getParticularMovie(movies[position])
        moviesViewModel.partMovie.observe(yourReviewFragment)
        {
            Log.i("order","$position ${it.get(0).movieName}")

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + it.get(0).moviePoster,holder.poster)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + it.get(0).moviePoster)
//                withContext(Dispatchers.Main) {
//                    holder.poster.setImageBitmap(bitmap)
//                }
//            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setNewData(newMovies: HashSet<Movies>)
    {
        movies = newMovies.toList()

        //movies.addAll(newMovies.toList())
        notifyDataSetChanged()
    }
}