package com.example.moviereview.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.MovieActivity
import kotlinx.coroutines.*


class SearchResultRvAdapter(val context: Context) : RecyclerView.Adapter<SearchResultRvAdapter.MyViewHolder>() {

    private var results:ArrayList<ShortMovieDesc> = arrayListOf()
    private var resultsMovies : ArrayList<Movies> = arrayListOf()
    class MyViewHolder(var itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        val poster = itemView.findViewById<ImageView>(R.id.poster_iv_search)
        val title = itemView.findViewById<TextView>(R.id.title_tv_search)
        val year = itemView.findViewById<TextView>(R.id.year_tv_search)
        val rating  = itemView.findViewById<TextView>(R.id.rating_tv_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_searchresult,parent,false)
        return SearchResultRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Log.i("usermovies",results.isEmpty().toString() + resultsMovies.isEmpty().toString())
        if(results.isEmpty()) {

            val currentItem = resultsMovies[position]
            if(currentItem.moviePoster?.isNotEmpty() == true) {
//                HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentItem.moviePoster,holder.poster)
                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentItem.moviePoster)
                    withContext(Dispatchers.Main) {
                        holder.poster.setImageBitmap(bitmap)
                    }
                }
            }
            else{
                holder.poster.setImageResource(R.drawable.poster_not)
            }
            holder.title.text =  currentItem.movieName
            if (currentItem.releaseDate!="") {
                holder.year.text = currentItem.releaseDate.substring(0,4)
            }
            holder.rating.text =  String.format("%.1f", currentItem.rating).toFloat().toString()
            holder.itemView.setOnClickListener {
                onClick(currentItem.movieId, currentItem.movieName, currentItem.rating)
            }
        }
        else
        {
//            Log.i("usermovies","inside")
            val currentItem = results[position]
            if(currentItem.poster?.isNotEmpty() == true) {
                HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentItem.poster,holder.poster)
//                CoroutineScope(Dispatchers.IO).launch {
//                    val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentItem.poster)
//                    withContext(Dispatchers.Main) {
//                        holder.poster.setImageBitmap(bitmap)
//                    }
//                }
            }
            else{
                holder.poster.setImageResource(R.drawable.poster_not)
            }
            holder.title.text =  currentItem.name
            if (!currentItem.releaseDate.isNullOrEmpty()) {
                holder.year.text = currentItem.releaseDate.substring(0,4)
            }
            holder.rating.text =  String.format("%.1f", currentItem.rating/2).toFloat().toString()

            holder.itemView.setOnClickListener {
                onClick(currentItem.movieId,currentItem.name,currentItem.rating)
            }

        }

    }

    private fun onClick(movieId: Int, name: String, rating: Float)
    {
//        Toast.makeText(context,movieId.toString(), Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MovieActivity::class.java)

        intent.putExtra("movieId",movieId)
        intent.putExtra("movieName",name)
        intent.putExtra("rating",rating/2)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        if(results.isEmpty())
        {
            return resultsMovies.size
        }
        return results.size
    }

    fun setNewResults(newResult: ArrayList<ShortMovieDesc>)
    {

            results = newResult
        notifyDataSetChanged()
    }

    fun setNewResultsMovies(newResult: ArrayList<Movies>)
    {
        resultsMovies = newResult
        notifyDataSetChanged()
    }
}