package com.example.moviereview.view.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityYoursBinding
import com.example.moviereview.db.local.viewmodel.ReviewsViewModel
import com.example.moviereview.view.adapter.YoursVpAdapter
import com.example.moviereview.view.fragments.AddListDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout


class YoursActivity : AppCompatActivity() {
    lateinit var binding : ActivityYoursBinding

    lateinit var reviewsViewModel: ReviewsViewModel

    lateinit var dialog: Dialog

    var index = MutableLiveData<Int>(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        findViewById<TextView>(R.id.actname_tv).text = "Yours"
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.plus_iv)?.setOnClickListener {
            val dialog = AddListDialog()
            dialog.show(supportFragmentManager,"add dialog")
        }

        setUpTabLayout()
    }

    private fun setUpTabLayout()
    {
        binding.yoursTl.addTab(binding.yoursTl.newTab().setText("Reviews"))
        binding.yoursTl.addTab(binding.yoursTl.newTab().setText("Lists"))

        binding.yoursVp.adapter = YoursVpAdapter(supportFragmentManager,lifecycle)

        binding.yoursTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.yoursVp.currentItem = tab?.position!!

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.yoursVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                index.value = position
                if(position.equals(0))
                {
                    findViewById<ImageView>(R.id.plus_iv)?.visibility = View.GONE
                }
                else{
                    findViewById<ImageView>(R.id.plus_iv)?.visibility = View.VISIBLE

                }
                binding.yoursTl.selectTab(binding.yoursTl.getTabAt(position))
            }
        })
    }

    override fun onBackPressed() {
        setResult(100, Intent())
        finish()
    }
}