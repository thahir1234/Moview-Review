package com.example.moviereview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Casts
import com.example.moviereview.db.remote.model.Genre
import java.util.*
import kotlin.collections.HashSet

class GenreRVAdapter(var context: Context): RecyclerView.Adapter<GenreRVAdapter.MyViewHolder>() {

    private var genres : kotlin.collections.List<Genre> = mutableListOf()
    class MyViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val genreName = itemView.findViewById<TextView>(R.id.genre_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_genre,parent,false)

        return GenreRVAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.genreName.text = genres[position].genreName
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    fun setNewData(newGenres : HashSet<Genre>)
    {
        genres = newGenres.toMutableList()
        notifyDataSetChanged()
    }

}