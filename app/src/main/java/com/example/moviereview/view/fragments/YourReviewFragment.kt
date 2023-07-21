package com.example.moviereview.view.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.FragmentYourReviewsBinding
import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.local.viewmodel.MoviesViewModel
import com.example.moviereview.db.local.viewmodel.ReviewsViewModel
import com.example.moviereview.view.adapter.YourReviewRvAdapter

class YourReviewFragment : Fragment() {

    lateinit var binding: FragmentYourReviewsBinding

    lateinit var moviesViewModel: MoviesViewModel
    lateinit var reviewsViewModel: ReviewsViewModel

    lateinit var sharedPreferences: SharedPreferences

    lateinit var email:String
    private var reviews : ArrayList<Reviews> = arrayListOf()

    lateinit var adapter: YourReviewRvAdapter

    lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourReviewsBinding.inflate(layoutInflater,container,false)

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        email = sharedPreferences.getString("email","").toString()

        adapter = YourReviewRvAdapter(requireContext(),this,this)



        reviewsViewModel.getReviewsByUser(email)
        reviewsViewModel.reviewsByUser.observe(requireActivity())
        {
            reviews.clear()
            for(i in it) {
                reviews.add(i)
            }
            if(reviews.size>0)
            {
                binding.yourReviewRv.visibility = View.VISIBLE
                binding.yourReviewNoData.visibility = View.GONE
            }
            else
            {
                binding.yourReviewNoData.visibility = View.VISIBLE
                binding.yourReviewRv.visibility = View.GONE
            }
            adapter.setNewReviews(reviews)
        }
        binding.yourReviewRv.adapter = adapter
        binding.yourReviewRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        return binding.root
    }

    private fun showBottomDialog()
    {
        dialog.findViewById<ImageView>(R.id.sort_your_review_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.sort_your_review_tick).setOnClickListener {
            val isAsc = dialog.findViewById<RadioButton>(R.id.asc_rb_your_review)
            val isDesc = dialog.findViewById<RadioButton>(R.id.desc_rb_your_review)

            val isName = dialog.findViewById<RadioButton>(R.id.name_rb_your_review)
            val isRating = dialog.findViewById<RadioButton>(R.id.rating_rb_your_review)
            val isDate = dialog.findViewById<RadioButton>(R.id.date_rb_your_review)
            if(isAsc.isChecked)
            {
                if(isName.isChecked)
                {
                    reviewsViewModel.isAsc = true
                    reviewsViewModel.isName = true
                    reviewsViewModel.isDsc = false
                    reviewsViewModel.isRating = false
                    reviewsViewModel.isDate = false
                }
                else if(isRating.isChecked)
                {
                    reviewsViewModel.isAsc = true
                    reviewsViewModel.isName = false
                    reviewsViewModel.isDsc = false
                    reviewsViewModel.isRating = true
                    reviewsViewModel.isDate = false
                }
                else if(isDate.isChecked)
                {
                    reviewsViewModel.isAsc = true
                    reviewsViewModel.isName = false
                    reviewsViewModel.isDsc = false
                    reviewsViewModel.isRating = false
                    reviewsViewModel.isDate = true
                }
                dialog.dismiss()
            }
            else if(isDesc.isChecked)
            {
                if(isName.isChecked)
                {
                    reviewsViewModel.isAsc = false
                    reviewsViewModel.isName = true
                    reviewsViewModel.isDsc = true
                    reviewsViewModel.isRating = false
                    reviewsViewModel.isDate = false
                }
                else if(isRating.isChecked)
                {
                    reviewsViewModel.isAsc = false
                    reviewsViewModel.isName = false
                    reviewsViewModel.isDsc = true
                    reviewsViewModel.isRating = true
                    reviewsViewModel.isDate = false
                }
                else if(isDate.isChecked)
                {
                    reviewsViewModel.isAsc = false
                    reviewsViewModel.isName = false
                    reviewsViewModel.isDsc = true
                    reviewsViewModel.isRating = false
                    reviewsViewModel.isDate = true
                }
                dialog.dismiss()
            }
            processSorting(dialog)
        }
        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
    }

    override fun onResume() {
        super.onResume()
        Log.i("TabFrag","YourReviewResumedt")
        dialog = Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_yours_review);

        requireActivity().findViewById<ImageView>(R.id.sort_iv).setOnClickListener {
            showBottomDialog()
        }

        processSorting(dialog)
    }
    private fun processSorting(dialog: Dialog)
    {

        var ascRb = dialog.findViewById<RadioButton>(R.id.asc_rb_your_review)
        var nameRb = dialog.findViewById<RadioButton>(R.id.name_rb_your_review)
        var descRb = dialog.findViewById<RadioButton>(R.id.desc_rb_your_review)
        var ratingRb = dialog.findViewById<RadioButton>(R.id.rating_rb_your_review)
        var dateRb = dialog.findViewById<RadioButton>(R.id.date_rb_your_review)

        if(reviewsViewModel.isAsc && reviewsViewModel.isName)
        {
            ascRb.isChecked = true
            nameRb.isChecked = true
            reviewsViewModel.allReviewsNameAsc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(reviewsViewModel.isAsc && reviewsViewModel.isRating)
        {
            ascRb.isChecked = true
            ratingRb.isChecked = true
            reviewsViewModel.allReviewsRatingAsc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(reviewsViewModel.isAsc && reviewsViewModel.isDate)
        {
            ascRb.isChecked = true
            dateRb.isChecked = true
            reviewsViewModel.allReviewsDateAsc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(reviewsViewModel.isDsc && reviewsViewModel.isName)
        {
            descRb.isChecked = true
            nameRb.isChecked = true
            reviewsViewModel.allReviewsNameDesc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(reviewsViewModel.isDsc && reviewsViewModel.isRating)
        {
            descRb.isChecked = true
            ratingRb.isChecked = true
            reviewsViewModel.allReviewsRatingDesc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(reviewsViewModel.isDsc && reviewsViewModel.isDate)
        {
            descRb.isChecked = true
            dateRb.isChecked = true
            reviewsViewModel.allReviewsDateDesc.observe(this)
            {
                setUpData(it)
            }
        }
    }

    private fun setUpData(it: List<Reviews>)
    {
        reviews.clear()
        for(i in it)
        {
            if(i.email == email)
            {
                Log.i("TabFrag","${i.movieName}")
                reviews.add(i)
            }
            if(reviews.size>0)
            {
                binding.yourReviewRv.visibility = View.VISIBLE
                binding.yourReviewNoData.visibility = View.GONE
            }
            else
            {
                binding.yourReviewNoData.visibility = View.VISIBLE
                binding.yourReviewRv.visibility = View.GONE
            }
            Log.i("TabFrag","${reviews.size} ${reviewsViewModel.isAsc} ${reviewsViewModel.isName} $email")
            adapter.setNewReviews(reviews)
        }
    }
}