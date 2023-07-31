package com.example.moviereview.view.list_screen_clean.presentation.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.view.list_screen_clean.presentation.viewmodel.ListMoviesViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.list_screen_clean.presentation.view.AddMovieActivity
import kotlinx.coroutines.*

class UserListRvAdapter(var context: Context,val listId:Long,val viewModelStoreOwner: ViewModelStoreOwner,val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<UserListRvAdapter.MyViewHolder>() {

    private var movies : MutableList<Int> = mutableListOf()

    lateinit var listMoviesViewModel: ListMoviesViewModel

    class MyViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var itemView2 = itemView.rootView
        var poster = itemView.findViewById<ImageView>(R.id.poster_iv)
        var number = itemView.findViewById<TextView>(R.id.list_movie_number)
        var cancel = itemView.findViewById<ImageView>(R.id.delete_movie_iv)
        var constLayout = itemView.findViewById<ConstraintLayout>(R.id.cons_movie_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_list_movie,parent,false)

        listMoviesViewModel = ViewModelProvider(viewModelStoreOwner).get(ListMoviesViewModel::class.java)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(position == movies.size)
        {
            holder.poster.setImageResource(R.drawable.plus_black)
            holder.poster.scaleType = ImageView.ScaleType.FIT_CENTER
            holder.number.text = ""
            holder.cancel.visibility = View.GONE

            holder.poster.setOnClickListener {
                Log.i("list_prob","$position ${movies.size}")
                if(position == movies.size)
                {
                    val intent = Intent(context, AddMovieActivity::class.java)
                    intent.putExtra("listId",listId)
                    context.startActivity(intent)
                }
            }
        }
        else
        {

            holder.poster.scaleType = ImageView.ScaleType.FIT_XY
            var thing = 0
            listMoviesViewModel.getMoviesByBoth(listId.toInt(),movies[position])
            listMoviesViewModel.moviesByBoth.observe(lifecycleOwner)
            {
//                Log.i("size",it.size.toString())
                if(it.size==1) {
                    val currentMovie = it.get(0)
                    holder.cancel.visibility = View.VISIBLE
                    if(currentMovie.poster.isNullOrEmpty())
                    {
                        holder.poster.setImageResource(R.drawable.poster_not)
                    }
                    else {
                        HelperFunction.loadImage(
                            context,
                            "https://image.tmdb.org/t/p/original/" + currentMovie.poster,
                            holder.poster
                        )
                    }
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovie.poster)
//                        withContext(Dispatchers.Main) {
//                            holder.poster.setImageBitmap(bitmap)
//                        }
//                    }
                    holder.number.text = (position + 1).toString()
                    holder.cancel.setOnClickListener {
                        AlertDialog.Builder(context).apply {
                            setTitle("Do you wish to delete?")
                            setMessage("You review will be deleted from the movie")
                            setPositiveButton("Delete",object: DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    listMoviesViewModel.deleteListMovie(listId.toInt(),currentMovie.movieId)
                                    thing = movies[position]
                                }
                            })
                            setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                }
                            })
                            show()
                        }

                    }
                }
                else
                {
                    movies.remove(thing)
                    notifyDataSetChanged()
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size+1
    }

    fun setNewData(newMovies: HashSet<Int>)
    {
        movies = newMovies.toMutableList()
        notifyDataSetChanged()
    }
}