package com.example.moviereview.view.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
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
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.LikedReviews
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.LikedReviewsViewModel
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.db.local.viewmodel.ReviewsViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.activities.MovieActivity
import com.example.moviereview.view.fragments.YourReviewFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YourReviewRvAdapter(var context: Context,var yourReviewFragment: YourReviewFragment,var lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<YourReviewRvAdapter.MyViewHolder>() {

    private var reviews : MutableList<Reviews> = arrayListOf()

    lateinit var moviesViewModel: MoviesViewModel
    lateinit var reviewsViewModel : ReviewsViewModel
    lateinit var likedReviewsViewModel: LikedReviewsViewModel

    lateinit var sharedPreferences: SharedPreferences

    private var isLikedReview = false
    lateinit var email:String

    class MyViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val poster = itemView.findViewById<ImageView>(R.id.your_review_profile)
        val title = itemView.findViewById<TextView>(R.id.your_review_title)
        val rating = itemView.findViewById<TextView>(R.id.your_review_rating)
        val content = itemView.findViewById<TextView>(R.id.your_review_content)
        val deleteBtn = itemView.findViewById<ImageView>(R.id.your_review_delete)
        val likeBtn = itemView.findViewById<ImageView>(R.id.your_review_like)
        val likeCount = itemView.findViewById<TextView>(R.id.your_review_like_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view:View = inflater.inflate(R.layout.item_recyclerview_yourreview,parent,false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        moviesViewModel = ViewModelProvider(yourReviewFragment).get(MoviesViewModel::class.java)
        reviewsViewModel = ViewModelProvider(yourReviewFragment).get(ReviewsViewModel::class.java)
        likedReviewsViewModel = ViewModelProvider(yourReviewFragment).get(LikedReviewsViewModel::class.java)

        return YourReviewRvAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentReview = reviews[position]

        moviesViewModel.getParticularMovie(currentReview.movieId)
        moviesViewModel.partMovie.observe(yourReviewFragment)
        {
            HelperFunction.loadImageGlide(context,"https://image.tmdb.org/t/p/original/" + it.get(0).moviePoster,holder.poster)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + it.get(0).moviePoster)
//                withContext(Dispatchers.Main) {
//                    holder.poster.setImageBitmap(bitmap)
//                }
//            }
            holder.title.text = it.get(0).movieName

            val movieId = it.get(0).movieId
            holder.title.setOnClickListener {
                val intent = Intent(context,MovieActivity::class.java)
                intent.putExtra("movieId",movieId)
                context.startActivity(intent)
            }
            holder.poster.setOnClickListener {
                val intent = Intent(context,MovieActivity::class.java)
                intent.putExtra("movieId",movieId)
                context.startActivity(intent)
            }
        }

        if(currentReview.content.trim() == "")
        {
            holder.content.background = null
        }
        else {
            holder.content.text = currentReview.content
            holder.content.setTypeface(holder.content.typeface, Typeface.NORMAL);
            holder.content.setOnClickListener {
                if(holder.content.ellipsize == null)
                {
                    holder.content.ellipsize = TextUtils.TruncateAt.END
                    holder.content.maxLines = 4
                }
                else
                {
                    holder.content.ellipsize = null
                    holder.content.maxLines = 199
                }
            }
        }
        holder.rating.text = currentReview.rating.toString()

        holder.deleteBtn.setOnClickListener {
            reviews.removeAt(position)
            reviewsViewModel.deleteReview(email = currentReview.email, movieId = currentReview.movieId)
            notifyItemRemoved(position);
            //notifyItemRangeChanged(0, reviews.size);
        }

        isLikedReview = false
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
                val toast  = Toast.makeText(context,"Unliked", Toast.LENGTH_SHORT)
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

    fun setNewReviews(newReviews:ArrayList<Reviews>)
    {
        reviews = newReviews.toMutableList()
        notifyDataSetChanged()
    }
}