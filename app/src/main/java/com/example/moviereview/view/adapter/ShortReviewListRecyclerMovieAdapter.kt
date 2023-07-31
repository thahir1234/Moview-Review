package com.example.moviereview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.google.android.material.imageview.ShapeableImageView
import java.util.LinkedList

class ShortReviewListRecyclerMovieAdapter (var context: Context,var vm:ViewModelStoreOwner,var lifecycleOwner: LifecycleOwner):RecyclerView.Adapter<ShortReviewListRecyclerMovieAdapter.MyViewHolder>(){

    private var reviews : List<Reviews> = LinkedList()

    lateinit var usersViewModel: UsersViewModel
    class MyViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val name = itemView.findViewById<TextView>(R.id.short_review_name_tv)
        val rating  = itemView.findViewById<TextView>(R.id.short_review_rating_tv)
        val desc = itemView.findViewById<TextView>(R.id.short_review_desc_tv)
        val pic = itemView.findViewById<ShapeableImageView>(R.id.your_review_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_review,parent,false)

        usersViewModel = ViewModelProvider(vm).get(UsersViewModel::class.java)
        return ShortReviewListRecyclerMovieAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentReview = reviews.get(position)

        holder.name.text = currentReview.name
        holder.rating.text = currentReview.rating.toString()
        holder.desc.text = if(currentReview.content.isNotEmpty())
        {
            currentReview.content
        }
        else{
            "-- No comments --"
        }

        if(currentReview.email.isNotEmpty()) {
            usersViewModel.getUserByEmail(currentReview.email)
            usersViewModel.userByEmail.observe(lifecycleOwner)
            {
                holder.pic.setImageBitmap(it.get(0).image)
            }
        }
        else
        {
            holder.pic.setImageResource(R.drawable.account_profile_black)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setNewReviews(newReviews: HashSet<Reviews>)
    {
        reviews = newReviews.toList()
        notifyDataSetChanged()
    }
}