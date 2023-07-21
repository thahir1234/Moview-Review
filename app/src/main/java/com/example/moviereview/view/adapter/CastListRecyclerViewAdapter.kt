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
import com.example.moviereview.db.local.entities.Casts
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.PeopleActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList

class CastListRecyclerViewAdapter(var context: Context):RecyclerView.Adapter<CastListRecyclerViewAdapter.MyViewHolder>() {

    private var casts:List<Casts> = LinkedList()
    class MyViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val castPic = itemView.findViewById<ImageView>(R.id.cast_iv)
        val castName = itemView.findViewById<TextView>(R.id.castname_tv)
        val characterName = itemView.findViewById<TextView>(R.id.charactename_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_cast,parent,false)

        return CastListRecyclerViewAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCast = casts[position]

        if(currentCast.profilePic != null)
        {
            HelperFunction.loadImageGlide(context,"https://image.tmdb.org/t/p/original/" + currentCast.profilePic,holder.castPic)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentCast.profilePic)
//                withContext(Dispatchers.Main) {
//                    holder.castPic.setImageBitmap(bitmap)
//                }
//            }
        }
        else
        {
            holder.castPic.setImageResource(R.drawable.person)
        }
        holder.castName.text = currentCast.castName
        holder.characterName.text = currentCast.characterName

        holder.itemView.setOnClickListener {
            val intent = Intent(context,PeopleActivity::class.java)
            intent.putExtra("castId",currentCast.castId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    fun setCasts(newCasts : HashSet<Casts>)
    {
        casts=newCasts.toList()
        notifyDataSetChanged()
    }
}