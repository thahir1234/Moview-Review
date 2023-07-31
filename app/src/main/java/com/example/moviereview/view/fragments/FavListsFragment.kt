package com.example.moviereview.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereview.databinding.FragmentFavListsBinding
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.db.local.viewmodel.LikedListViewModel
import com.example.moviereview.view.list_screen_clean.presentation.viewmodel.ListMoviesViewModel
import com.example.moviereview.db.local.viewmodel.ListViewModel
import com.example.moviereview.view.adapter.ListsRecyclerViewAdapter

class FavListsFragment : Fragment() {

    lateinit var binding:FragmentFavListsBinding

    lateinit var sharedPreferences: SharedPreferences

    lateinit var likedListViewModel: LikedListViewModel
    lateinit var listViewModel: ListViewModel
    lateinit var listMoviesViewModel: ListMoviesViewModel

    private var email = ""

    private var lists : ArrayList<Lists> = arrayListOf()
    private var listAndMovies : LinkedHashMap<Lists,HashSet<ListMovies>> = linkedMapOf()

    lateinit var adapter : ListsRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavListsBinding.inflate(layoutInflater,container,false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        likedListViewModel = ViewModelProvider(this).get(LikedListViewModel::class.java)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listMoviesViewModel = ViewModelProvider(this).get(ListMoviesViewModel::class.java)

        email = sharedPreferences.getString("email","").toString()

        adapter = ListsRecyclerViewAdapter(requireContext(),this,this)

        if(email.isNotEmpty())
        {
            likedListViewModel.getLikedListsByEmail(email)
            likedListViewModel.likedListsByEmail.observe(viewLifecycleOwner)
            {
                listAndMovies.clear()
                Log.i("favList",it.size.toString())
                if(it.size>0)
                {
                    binding.favListRv.visibility = View.VISIBLE
                    binding.favListNoDataTv.visibility = View.GONE
                }
                else{
                    binding.favListRv.visibility = View.GONE
                    binding.favListNoDataTv.visibility = View.VISIBLE
                }
                for(i in it)
                {
                    listViewModel.getListsByListId(i.listId)
                    listViewModel.allListsByListId.observe(viewLifecycleOwner)
                    {
                        val currentList = it.get(0)
                        listAndMovies[currentList] = hashSetOf()
                        lists.add(it.get(0))
                        listMoviesViewModel.getListMovies(it.get(0).listId)
                        listMoviesViewModel.listMoviesByListId.observe(viewLifecycleOwner)
                        {
                            for(j in it) {
                                listAndMovies[currentList]?.add(j)
                            }
                            adapter.setNewData(listAndMovies)
                        }
                    }
                }
            }
        }
        binding.favListRv.adapter = adapter
        binding.favListRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}