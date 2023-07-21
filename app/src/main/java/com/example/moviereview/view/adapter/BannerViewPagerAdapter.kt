package com.example.moviereview.view.adapter

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.LoadedStatusViewModel
import com.example.moviereview.utils.HelperFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList

class BannerViewPagerAdapter(var context: Context,var loadedStatusViewModel: LoadedStatusViewModel): RecyclerView.Adapter<BannerViewPagerAdapter.ViewPagerViewHolder>() {

    private var movies : List<Movies> = LinkedList()

    inner class ViewPagerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var iv: ImageView = itemView.findViewById<ImageView>(R.id.banner_iv)
        var name = itemView.findViewById<TextView>(R.id.title_tv)
        var year = itemView.findViewById<TextView>(R.id.year_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager_movie,parent,false)
        val iv = view.findViewById<ImageView>(R.id.banner_iv)
        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val desiredHeight = (screenHeight*0.275).toInt()

        iv.layoutParams.height = desiredHeight
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        if(movies.size>0) {
            val currentMovie = movies.get(position % movies.size)
            HelperFunction.loadImageGlide(context,"https://image.tmdb.org/t/p/original/" + currentMovie.movieBanner,holder.iv)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovie.movieBanner)
//                withContext(Dispatchers.Main) {
//                    holder.iv.setImageBitmap(bitmap)
//                }
//            }
            holder.name.text = currentMovie.movieName
            holder.year.text = currentMovie.releaseDate.substring(0,4)
        }
//        if(position==images.size-1)
//        {
//            viewPager2.post(runnable)
//        }
    }

    override fun getItemCount(): Int {
        if(movies.size>0) {
            return 100
        }
        return 0
    }


    fun setNewData(newMovies: HashSet<Movies>)
    {
        movies = newMovies.toList()
        loadedStatusViewModel.updatePopular()
        notifyDataSetChanged()
    }

}