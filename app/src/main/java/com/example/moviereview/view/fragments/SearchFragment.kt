package com.example.moviereview.view.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.databinding.FragmentSearchBinding
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.db.local.entities.Movies
import com.example.moviereview.db.local.viewmodel.ListMoviesViewModel
import com.example.moviereview.db.local.viewmodel.ListViewModel
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.db.remote.model.MovieList
import com.example.moviereview.db.remote.model.ShortMovieDesc
import com.example.moviereview.view.adapter.ListsRecyclerViewAdapter
import com.example.moviereview.view.adapter.SearchResultRvAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding

    private var searchResults : LiveData<MovieList>? = null

    private lateinit var searchListAdapter: SearchResultRvAdapter
    private lateinit var listAdapter : ListsRecyclerViewAdapter

    private lateinit var searchResultArray : ArrayList<ShortMovieDesc>

    lateinit var listViewModel: ListViewModel
    lateinit var listMoviesViewModel: ListMoviesViewModel

    private var lists : ArrayList<Lists> = arrayListOf()
    private var listMovies: ArrayList<ArrayList<ListMovies>> = arrayListOf()

    private var listAndMovies : LinkedHashMap<Lists,HashSet<ListMovies>> = linkedMapOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listMoviesViewModel = ViewModelProvider(this).get(ListMoviesViewModel::class.java)
        searchListAdapter = SearchResultRvAdapter(requireContext())
        listAdapter = ListsRecyclerViewAdapter(requireContext(),requireActivity(),requireActivity())
        searchResultArray = arrayListOf()

        binding.searchresultRv.adapter = searchListAdapter
        binding.searchresultRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.searchresultRv.visibility = View.GONE
        binding.listsTv.visibility = View.GONE

        binding.searchEt.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchResultArray=arrayListOf()
                binding.listsTv.visibility = View.GONE
                binding.listsRvSearch.visibility = View.GONE
                binding.searchresultRv.visibility = View.VISIBLE
                if (query != null) {
                    searchResults = APIImplementation.searchMovies(query.trim())
                    searchResults?.observe(viewLifecycleOwner)
                    {
                        for(i in 0 until it.results.size) {
                            searchResultArray.add(it.results.get(i))
                        }
                        searchListAdapter.setNewResults(searchResultArray)
                        //Log.i("search", it.results.size.toString())
                    }

                    binding.searchEt.clearFocus()
                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("search",newText.toString())
                if(newText==null || newText=="")
                {
                    binding.listsRvSearch.visibility = View.VISIBLE
                    binding.searchresultRv.visibility = View.GONE
                }
                else
                {
                    searchResultArray=arrayListOf()
                    binding.listsTv.visibility = View.GONE
                    binding.listsRvSearch.visibility = View.GONE
                    binding.searchresultRv.visibility = View.VISIBLE
                    searchResults = APIImplementation.searchMovies(newText.trim())

                    searchResults?.observe(viewLifecycleOwner)
                    {
                        for(i in 0 until it.results.size) {
                            searchResultArray.add(it.results.get(i))
                        }
                        searchListAdapter.setNewResults(searchResultArray)
                        //Log.i("search", it.results.size.toString())
                    }
                }
                return true
            }

        })
        setUpListRv()
        return binding.root
    }

    private fun setUpListRv()
    {

        listViewModel.allLists.observe(requireActivity())
        {
            for (i in it)
            {
                if(i.visibility == "Public") {
                    lists.add(i)
                    listAndMovies[i] = hashSetOf()
                    var dummy: ArrayList<ListMovies> = arrayListOf()
                    listMoviesViewModel.getListMovies(i.listId)
                    listMoviesViewModel.listMoviesByListId.observe(requireActivity())
                    {
                        for (j in it) {
                            dummy.add(j)
                            listAndMovies[i]?.add(j)
                        }
                        listMovies.add(dummy)
                        listAdapter.setNewData(listAndMovies)
                    }
                }
            }
            if(listAndMovies.isEmpty())
            {
                binding.listsTv.visibility = View.GONE
            }
            else
            {
                binding.listsTv.visibility = View.VISIBLE

            }
            Log.i("Public","${listAndMovies.size}")
        }
        binding.listsRvSearch.adapter = listAdapter
        binding.listsRvSearch.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }
}