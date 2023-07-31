package com.example.moviereview.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.LoadedStatusViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.MovieActivity
import kotlinx.coroutines.*

class MovieListRecyclerViewAdapter(
    var context: Context,
    var loadedStatusViewModel: LoadedStatusViewModel
) :
    RecyclerView.Adapter<MovieListRecyclerViewAdapter.MyViewHolder>() {

    private var movies:List<Movies> = arrayListOf()
    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView){
        var poster:ImageView = itemView.findViewById(R.id.poster_iv)
        var rating:TextView = itemView.findViewById(R.id.rating_tv)
        var name:TextView = itemView.findViewById(R.id.name_tv_rv)
        var year:TextView = itemView.findViewById(R.id.year_tv_rv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_movie,parent,false)

        return MovieListRecyclerViewAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(position==1) {
            loadedStatusViewModel.updateTrending()
        }
        val currentItem = movies[position]
        holder.name.text = currentItem.movieName
        holder.rating.text = String.format("%.1f", currentItem.rating).toFloat().toString()
        if(currentItem.releaseDate.isNotEmpty() && currentItem.releaseDate.isNotBlank()) {
            holder.year.text = currentItem.releaseDate.substring(0, 4)
        }
        if(currentItem.moviePoster?.isNotEmpty() == true) {
//            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentItem.moviePoster,holder.poster)
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentItem.moviePoster)
                withContext(Dispatchers.Main) {
                    holder.poster.setImageBitmap(bitmap)
                }
            }
//            DownloadImageTask(holder.poster).execute("https://image.tmdb.org/t/p/original/" + currentItem.moviePoster)
        }
        else{
            holder.poster.setImageResource(R.drawable.poster_not)
        }
        holder.itemView.setOnClickListener {
//            Toast.makeText(context,currentItem.movieId.toString(),Toast.LENGTH_SHORT).show()
            val intent = Intent(context,MovieActivity::class.java)

            intent.putExtra("movieId",currentItem.movieId)
            intent.putExtra("rating",holder.rating.text.toString().toFloat())
            intent.putExtra("movieName",currentItem.movieName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if(movies.size>5)
        {
            return 5
        }
        return movies.size
    }



    fun setNewData(newMovies: HashSet<Movies>)
    {
        movies = newMovies.toList()
        notifyDataSetChanged()
    }
}