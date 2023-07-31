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
import com.example.moviereview.databinding.FragmentYoursListsBinding
import com.example.moviereview.db.local.entities.ListMovies
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.view.list_screen_clean.presentation.viewmodel.ListMoviesViewModel
import com.example.moviereview.db.local.viewmodel.ListViewModel
import com.example.moviereview.view.adapter.ListsRecyclerViewAdapter

class YoursListsFragment: Fragment() {

    lateinit var binding: FragmentYoursListsBinding

    lateinit var listMoviesViewModel: ListMoviesViewModel
    lateinit var listViewModel: ListViewModel

    private var lists : ArrayList<Lists> = arrayListOf()
    private var listMovies: ArrayList<ArrayList<ListMovies>> = arrayListOf()

    private var listAndMovies : LinkedHashMap<Lists,HashSet<ListMovies>> = LinkedHashMap<Lists,HashSet<ListMovies>>()

    lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter : ListsRecyclerViewAdapter

    lateinit var dialog:Dialog
    private var email =""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYoursListsBinding.inflate(layoutInflater,container,false)

        listViewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        listMoviesViewModel = ViewModelProvider(requireActivity()).get(ListMoviesViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        email = sharedPreferences.getString("email","").toString()

        adapter = ListsRecyclerViewAdapter(requireContext(),this,this)


        setUpListRv()

        return binding.root
    }

    private fun setUpListRv()
    {
        listAndMovies.clear()
        listViewModel.getListsByEmail(email)
        listViewModel.allListsByEmail.observe(requireActivity())
        {

            Log.i("listRv","allListByEmail() called")
            for (i in it)
            {
                listAndMovies[i] = hashSetOf()
                var dummy:ArrayList<ListMovies> = arrayListOf()
                lists.add(i)
                listMoviesViewModel.getListMovies(i.listId)
                listMoviesViewModel.listMoviesByListId.observe(requireActivity())
                {
                    for(j in it) {
                        dummy.add(j)
                        listAndMovies[i]?.add(j)
                    }
                    listMovies.add(dummy)
                    if(listAndMovies.isEmpty())
                    {
                        binding.yourListRv.visibility = View.GONE
                        binding.yourListNoData.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.yourListRv.visibility = View.VISIBLE
                        binding.yourListNoData.visibility = View.GONE
                    }
                    adapter.setNewData(listAndMovies)
                }

            }
        }
        binding.yourListRv.adapter = adapter
        binding.yourListRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }

    private fun showBottomDialog()
    {
        dialog.findViewById<ImageView>(R.id.sort_your_list_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.sort_your_list_tick).setOnClickListener {
            Log.i("Lists","tick clicked")
            val isAsc = dialog.findViewById<RadioButton>(R.id.asc_rb_your_list)
            val isDesc = dialog.findViewById<RadioButton>(R.id.desc_rb_your_list)

            val isName = dialog.findViewById<RadioButton>(R.id.name_rb_your_list)
            val isRating = dialog.findViewById<RadioButton>(R.id.rating_rb_your_list)
            val isDate = dialog.findViewById<RadioButton>(R.id.date_rb_your_list)
            if(isAsc.isChecked)
            {
                if(isName.isChecked)
                {
                    listViewModel.isAsc = true
                    listViewModel.isName = true
                    listViewModel.isDsc = false
                    listViewModel.isRating = false
                    listViewModel.isDate = false
                }
                else if(isRating.isChecked)
                {
                    listViewModel.isAsc = true
                    listViewModel.isName = false
                    listViewModel.isDsc = false
                    listViewModel.isRating = true
                    listViewModel.isDate = false
                }
                else if(isDate.isChecked)
                {
                    listViewModel.isAsc = true
                    listViewModel.isName = false
                    listViewModel.isDsc = false
                    listViewModel.isRating = false
                    listViewModel.isDate = true
                }
                dialog.dismiss()
            }
            else if(isDesc.isChecked)
            {
                if(isName.isChecked)
                {
                    listViewModel.isAsc = false
                    listViewModel.isName = true
                    listViewModel.isDsc = true
                    listViewModel.isRating = false
                    listViewModel.isDate = false
                }
                else if(isRating.isChecked)
                {
                    listViewModel.isAsc = false
                    listViewModel.isName = false
                    listViewModel.isDsc = true
                    listViewModel.isRating = true
                    listViewModel.isDate = false
                }
                else if(isDate.isChecked)
                {
                    listViewModel.isAsc = false
                    listViewModel.isName = false
                    listViewModel.isDsc = true
                    listViewModel.isRating = false
                    listViewModel.isDate = true
                }
                dialog.dismiss()
            }
            processSorting(dialog)
        }
        Log.i("Lists","bottom opened")
        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
    }

    private fun processSorting(dialog: Dialog)
    {
        var ascRb = dialog.findViewById<RadioButton>(R.id.asc_rb_your_list)
        var nameRb = dialog.findViewById<RadioButton>(R.id.name_rb_your_list)
        var descRb = dialog.findViewById<RadioButton>(R.id.desc_rb_your_list)
        var ratingRb = dialog.findViewById<RadioButton>(R.id.rating_rb_your_list)
        var dateRb = dialog.findViewById<RadioButton>(R.id.date_rb_your_list)

        Log.i("Lists","${listViewModel.isAsc} ${listViewModel.isName}")
        if(listViewModel.isAsc && listViewModel.isName)
        {
            ascRb.isChecked = true
            nameRb.isChecked = true
            listViewModel.allListsTitleAsc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(listViewModel.isAsc && listViewModel.isRating)
        {
            ascRb.isChecked = true
            ratingRb.isChecked = true

        }
        else if(listViewModel.isAsc && listViewModel.isDate)
        {
            ascRb.isChecked = true
            dateRb.isChecked = true
            listViewModel.allListsDateAsc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(listViewModel.isDsc && listViewModel.isName)
        {
            descRb.isChecked = true
            nameRb.isChecked = true
            listViewModel.allListsTitleDesc.observe(this)
            {
                setUpData(it)
            }
        }
        else if(listViewModel.isDsc && listViewModel.isRating)
        {
            descRb.isChecked = true
            ratingRb.isChecked = true
        }
        else if(listViewModel.isDsc && listViewModel.isDate)
        {
            descRb.isChecked = true
            dateRb.isChecked = true
            listViewModel.allListsDateDesc.observe(this)
            {
                setUpData(it)
            }
        }
    }

    private fun setUpData(it: List<Lists>)
    {
        listAndMovies.clear()
        for (i in it)
        {
            if(i.email == email) {
//                Log.i("Lists",i.title)
                listAndMovies[i] = hashSetOf()
                var dummy: ArrayList<ListMovies> = arrayListOf()
                lists.add(i)
                listMoviesViewModel.getListMovies(i.listId)
                listMoviesViewModel.listMoviesByListId.observe(requireActivity())
                {
                    for (j in it) {
                        dummy.add(j)
                        listAndMovies[i]?.add(j)
                    }
                    listMovies.add(dummy)
                    if (listAndMovies.isEmpty()) {
                        binding.yourListRv.visibility = View.GONE
                        binding.yourListNoData.visibility = View.VISIBLE
                    } else {
                        binding.yourListRv.visibility = View.VISIBLE
                        binding.yourListNoData.visibility = View.GONE
                    }
                    for(i in listAndMovies.keys)
                    {
                        Log.i("Lists",i.title)

                    }
                    adapter.setNewData(listAndMovies)
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        dialog = Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_yours_list);

        requireActivity().findViewById<ImageView>(R.id.sort_iv).setOnClickListener {
            showBottomDialog()
        }
        processSorting(dialog)
    }
}