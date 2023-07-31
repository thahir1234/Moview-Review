package com.example.moviereview.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviereview.view.fragments.YourReviewFragment
import com.example.moviereview.view.fragments.YoursListsFragment

class YoursVpAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position)
        {
            0->
            {
                return YourReviewFragment()
            }
            1->
            {
                return YoursListsFragment()
            }
            else->
            {
                return YourReviewFragment()
            }
        }
    }

}