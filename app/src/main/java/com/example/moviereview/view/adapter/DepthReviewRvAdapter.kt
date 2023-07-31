package com.example.moviereview.view.adapter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.LikedReviews
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.LikedReviewsViewModel
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction
import com.google.android.material.imageview.ShapeableImageView
import kotlin.collections.HashSet

class DepthReviewRvAdapter(var context: Context, var vm: ViewModelStoreOwner, var lifecycleOwner: LifecycleOwner):
    RecyclerView.Adapter<DepthReviewRvAdapter.MyViewHolder>(){

    private var reviews : kotlin.collections.MutableList<Reviews> = arrayListOf()

    lateinit var sharedPreferences: SharedPreferences
    lateinit var usersViewModel: UsersViewModel
    lateinit var likedReviewsViewModel : LikedReviewsViewModel

    private var email = ""
    class MyViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val name = itemView.findViewById<TextView>(R.id.depth_review_name_tv)
        val rating  = itemView.findViewById<TextView>(R.id.depth_review_rating_tv)
        val desc = itemView.findViewById<TextView>(R.id.depth_review_desc_tv)
        val pic = itemView.findViewById<ShapeableImageView>(R.id.depth_review_profile)
        val likeCount = itemView.findViewById<TextView>(R.id.depth_review_like_count)
        val likeBtn = itemView.findViewById<ImageView>(R.id.depth_review_like_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_depthreview,parent,false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        usersViewModel = ViewModelProvider(vm).get(UsersViewModel::class.java)
        likedReviewsViewModel = ViewModelProvider(vm).get(LikedReviewsViewModel::class.java)

        return DepthReviewRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentReview = reviews.get(position)

        holder.name.text = currentReview.name
        holder.rating.text = currentReview.rating.toString()
        holder.desc.text = if(currentReview.content.length>0)
        {
            currentReview.content
        }
        else
        {
            "-- No comments --"
        }

        holder.desc.setOnClickListener {
            if(holder.desc.ellipsize == null)
            {
                holder.desc.ellipsize = TextUtils.TruncateAt.END
                holder.desc.maxLines = 3
            }
            else
            {
                holder.desc.ellipsize = null
                holder.desc.maxLines = 199
            }
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
        var isLikedReview = false
        email = sharedPreferences.getString("email", "").toString()

        likedReviewsViewModel.getCountByReviews(currentReview.reviewId)
        likedReviewsViewModel.countByReviews.observe(lifecycleOwner)
        {
            holder.likeCount.text = it.size.toString()
            for( i in it)
            {
                if(i.email == email)
                {
                    holder.likeBtn.setImageResource(R.drawable.thumpsup)
                    isLikedReview = true
                    setUpLikeReviewBtn(holder.likeBtn,currentReview,holder.likeCount,isLikedReview)

                }
            }
        }
        setUpLikeReviewBtn(holder.likeBtn,currentReview,holder.likeCount,isLikedReview)
    }

    private fun setUpLikeReviewBtn(
        likeBtn: ImageView,
        currentReview: Reviews,
        likeCount: TextView,
        isLikedReview: Boolean,)
    {
        var isLiked = isLikedReview
        likeBtn.setOnClickListener {
            if(!isLiked) {
                val toast = Toast.makeText(context,"Liked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,context.resources)
                likedReviewsViewModel.addLikedReview(LikedReviews(email, currentReview.reviewId))
                likeBtn.setImageResource(R.drawable.thumpsup)
                isLiked =true
            }
            else
            {
                val toast = Toast.makeText(context,"Unliked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,context.resources)
                likedReviewsViewModel.deleteLikedReview(email,currentReview.reviewId)
                likeBtn.setImageResource(R.drawable.thumpups_off)
                isLiked =false
            }
        }

    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setNewDate(newReviews:HashSet<Reviews>)
    {
        reviews = newReviews.toMutableList()
        notifyDataSetChanged()
    }
}