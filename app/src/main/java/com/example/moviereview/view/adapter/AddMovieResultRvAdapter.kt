package com.example.moviereview.view.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.ListMoviesViewModel
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMovieResultRvAdapter (val context: Context,val listId:Long,val viewModelStoreOwner :ViewModelStoreOwner,val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<AddMovieResultRvAdapter.MyViewHolder>() {

    private var results:ArrayList<ShortMovieDesc> = arrayListOf()

    lateinit var listMovieViewModel : ListMoviesViewModel
    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val poster = itemView.findViewById<ImageView>(R.id.poster_iv_search)
        val title = itemView.findViewById<TextView>(R.id.title_tv_search)
        val year = itemView.findViewById<TextView>(R.id.year_tv_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_searchresult,parent,false)

        listMovieViewModel = ViewModelProvider(viewModelStoreOwner).get(ListMoviesViewModel::class.java)
        return AddMovieResultRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = results[position]
        HelperFunction.loadImageGlide(context,"https://image.tmdb.org/t/p/original/" + currentItem.poster,holder.poster)
//        CoroutineScope(Dispatchers.IO).launch {
//            val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentItem.poster)
//            withContext(Dispatchers.Main) {
//               holder.poster.setImageBitmap(bitmap)
//            }
//        }
        holder.title.text =  currentItem.name
        if (currentItem.releaseDate.length>=1) {
            holder.year.text = currentItem.releaseDate.substring(0,4)
        }

        holder.itemView.setOnClickListener {
            listMovieViewModel.getListMovies(listId.toInt())
            listMovieViewModel.listMoviesByListId.observe(lifecycleOwner)
            {
                var flag = 1
                for(i in it)
                {
                    if(i.movieId == currentItem.movieId)
                    {
                        val toast = Toast.makeText(context,"Already added",Toast.LENGTH_SHORT)
                        HelperFunction.showToast(toast,context.resources)
                        flag = 0
                    }
                }
                if(flag == 1)
                {
                    AlertDialog.Builder(context).apply {
                        setTitle("Confirm Add")
                        setMessage("\"${currentItem.name}\" movie will be added to your list")
                        setPositiveButton("confirm",object: DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                listMovieViewModel.addListMovie(ListMovies(listId.toInt(),currentItem.movieId,currentItem.poster))
                                val toast = Toast.makeText(context,"Added!",Toast.LENGTH_SHORT)
                                HelperFunction.showToast(toast,context.resources)
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
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun setNewResults(newResult: ArrayList<ShortMovieDesc>)
    {
        results = newResult
        notifyDataSetChanged()
    }
}