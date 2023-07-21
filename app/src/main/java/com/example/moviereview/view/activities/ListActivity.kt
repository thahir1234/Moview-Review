package com.example.moviereview.view.activities

import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityListBinding
import com.example.moviereview.db.local.viewmodel.ListMoviesViewModel
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.view.adapter.UserListRvAdapter

class ListActivity : AppCompatActivity() {

    lateinit var binding : ActivityListBinding

    lateinit var usersViewModel: UsersViewModel
    lateinit var listMoviesViewModel: ListMoviesViewModel

    lateinit var listName : String
    lateinit var listDesc : String
    lateinit var email :String
    var listId : Long = -1
    var listDate:String = ""

    lateinit var adapter : UserListRvAdapter

    lateinit var sharedPreferences: SharedPreferences

    var  movies:HashSet<Int> = hashSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        listMoviesViewModel = ViewModelProvider(this).get(ListMoviesViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        email = sharedPreferences.getString("email","").toString()
        listName = intent.getStringExtra("listName").toString()
        listDesc = intent.getStringExtra("listDesc").toString()
        listId = intent.getLongExtra("listId",-1)
        listDate = intent.getStringExtra("listDate").toString()

        findViewById<TextView>(R.id.actname_tv).text = listName
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }

        Toast.makeText(this,listId.toString(),Toast.LENGTH_SHORT).show()
        adapter = UserListRvAdapter(this,listId,this,this)

        setUpList()
    }

    private fun setUpList()
    {
        binding.listTitleTv.text = listName
        binding.listDaysTv.text = listDate
        usersViewModel.getUserByEmail(email)
        usersViewModel.userByEmail.observe(this)
        {
            binding.listOwnerIv.setImageBitmap(it.get(0).image)
            binding.listOwnernameTv.text = it.get(0).name
        }

        listMoviesViewModel.getListMovies(listId.toInt())
        listMoviesViewModel.listMoviesByListId.observe(this)
        {
            for (i in it) {
                movies.add(i.movieId)
                adapter.setNewData(movies)
            }
        }
        binding.listMoviesRv.adapter = adapter
        binding.listMoviesRv.layoutManager = GridLayoutManager(this,2)
        addGapBetween()
//        binding.listMoviesRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }

    private fun addGapBetween()
    {
        val spacing = 40
        binding.listMoviesRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

                // Add spacing between items based on their span index
                if (spanIndex == 0) {
                    // For items in the left column, add spacing to the right
                    outRect.right = spacing
                } else {
                    // For items in the right column, add spacing to the left
                    outRect.left = spacing
                }

                // Add spacing at the top and bottom of each item
                outRect.top = spacing
                outRect.bottom = spacing
            }
        })
    }
}