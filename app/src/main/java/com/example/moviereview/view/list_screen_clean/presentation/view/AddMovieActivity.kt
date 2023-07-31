package com.example.moviereview.view.list_screen_clean.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityAddMovieBinding
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.view.list_screen_clean.data.api.dto.MovieList
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.view.list_screen_clean.data.api.MovieSearchApiImpl
import com.example.moviereview.view.list_screen_clean.presentation.adapter.AddMovieResultRvAdapter

class AddMovieActivity : AppCompatActivity() {

    lateinit var binding : ActivityAddMovieBinding

    lateinit var searchResultsArray : ArrayList<ShortMovieDesc>
    private var searchResults : LiveData<MovieList>? = null

    private lateinit var searchListAdapter: AddMovieResultRvAdapter

    private var listId :Long= -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listId = intent.getLongExtra("listId",-1)

        findViewById<TextView>(R.id.actname_tv).text = "Add Movie"
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }

        searchListAdapter = AddMovieResultRvAdapter(this,listId,this,this)

        searchResultsArray = arrayListOf()

        binding.addSearchresultRv.adapter = searchListAdapter
        binding.addSearchresultRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.addSearchEt.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null)
                {
                    searchResultsArray =arrayListOf()

                    searchResults = MovieSearchApiImpl.searchMovies(query.trim())

                    searchResults?.observe(this@AddMovieActivity)
                    {
                        for(i in 0 until it.results.size) {
                            searchResultsArray.add(it.results.get(i))
                        }
                        searchListAdapter.setNewResults(searchResultsArray)
                        //Log.i("search", it.results.size.toString())
                    }

                    binding.addSearchEt.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("search",newText.toString())
                if(newText==null || newText=="")
                {
                    binding.addSearchresultRv.visibility = View.GONE
                }
                else{
                    binding.addSearchresultRv.visibility = View.VISIBLE
                }
                return true
            }

        })
    }

}