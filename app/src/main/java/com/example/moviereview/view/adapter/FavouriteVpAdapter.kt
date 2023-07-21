package com.example.moviereview.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviereview.view.fragments.FavListsFragment
import com.example.moviereview.view.fragments.FavMovieFragment
import com.google.android.material.search.SearchView.Behavior

class FavouriteVpAdapter(fm: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position)
        {
            0->
            {
                return FavMovieFragment()
            }
            1->
            {
                return FavListsFragment()
            }
            else->
            {
                return FavMovieFragment()
            }
        }
    }

}