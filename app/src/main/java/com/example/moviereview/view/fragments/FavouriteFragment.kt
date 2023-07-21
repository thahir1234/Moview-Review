package com.example.moviereview.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.moviereview.databinding.FragmentFavListsBinding
import com.example.moviereview.databinding.FragmentFavouriteBinding
import com.example.moviereview.view.adapter.FavouriteVpAdapter
import com.google.android.material.tabs.TabLayout

class FavouriteFragment : Fragment() {
    lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(layoutInflater,container,false)

        binding.sortFavIv.visibility =View.GONE
        setUpTabLayout()

        return binding.root
    }

    private fun setUpTabLayout()
    {
        binding.favouriteTl.addTab(binding.favouriteTl.newTab().setText("Movies"))
        binding.favouriteTl.addTab(binding.favouriteTl.newTab().setText("Lists"))

        binding.favouriteVp.adapter = FavouriteVpAdapter(childFragmentManager,lifecycle)

        binding.favouriteTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.favouriteVp.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.favouriteVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.favouriteTl.selectTab(binding.favouriteTl.getTabAt(position))
            }
        })
    }
}