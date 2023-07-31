package com.example.moviereview.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.MovieActivity
import com.example.moviereview.view.fragments.FavMovieFragment
import java.util.LinkedList
import kotlinx.coroutines.*

class FavMoviesRvAdapter(val context: Context,val favMovieFragment: FavMovieFragment): RecyclerView.Adapter<FavMoviesRvAdapter.MyViewHolder>() {

    private var favMovies : List<Int>  = LinkedList()

    lateinit var moviesViewModel:MoviesViewModel
    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val poster = itemView.findViewById<ImageView>(R.id.poster_iv_search)
        val title = itemView.findViewById<TextView>(R.id.title_tv_search)
        val year = itemView.findViewById<TextView>(R.id.year_tv_search)
        val rating = itemView.findViewById<TextView>(R.id.rating_tv_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_searchresult,parent,false)

        moviesViewModel = ViewModelProvider(favMovieFragment).get(MoviesViewModel::class.java)
        return FavMoviesRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("favMov","onBInd")
        val movieId = favMovies[position]
        moviesViewModel.getParticularMovie(movieId)
        moviesViewModel.partMovie.observe(favMovieFragment)
        {
            for(i in it) {
                val currentItem = i
                if(currentItem.moviePoster?.isNotEmpty() == true) {
                    HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentItem.moviePoster,holder.poster)
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentItem.moviePoster)
//                        withContext(Dispatchers.Main) {
//                            holder.poster.setImageBitmap(bitmap)
//                        }
//                    }
                }
                else{
                    holder.poster.setImageResource(R.drawable.poster_not)
                }

                holder.title.text = currentItem.movieName
                if (currentItem.releaseDate.length >= 1) {
                    holder.year.text = currentItem.releaseDate.substring(0, 4)
                }
                holder.rating.text =  String.format("%.1f", currentItem.rating).toFloat().toString()

                holder.itemView.setOnClickListener {
                    Toast.makeText(context, currentItem.movieId.toString(), Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(context, MovieActivity::class.java)

                    intent.putExtra("movieId", currentItem.movieId)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        Log.i("favMov",favMovies.size.toString())
        return favMovies.size
    }

//    fun setNewFavMovies(newFavMovies:ArrayList<Movies>)
//    {
//        favMovies = newFavMovies
//        notifyDataSetChanged()
//    }

    fun setNewFavMovies(newFavMovies:HashSet<Int>)
    {
        favMovies = newFavMovies.toList()
        notifyDataSetChanged()
    }

}