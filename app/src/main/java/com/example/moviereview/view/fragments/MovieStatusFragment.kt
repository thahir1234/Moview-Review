package com.example.moviereview.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.UserMovies
import com.example.moviereview.db.local.viewmodel.UserMoviesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MovieStatusFragment(val movieId : Int,var position:Int) : DialogFragment() {


    lateinit var sharedPreferences : SharedPreferences

    lateinit var userMoviesViewModel: UserMoviesViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder:AlertDialog.Builder = AlertDialog.Builder(requireContext())

        userMoviesViewModel = ViewModelProvider(this).get(UserMoviesViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val values = requireContext().resources.getStringArray(R.array.movie_status)


        builder.setTitle("Select status")
            .setSingleChoiceItems(values,position,object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    position = which
                }
            })
            .setPositiveButton("Ok",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val email: String? = sharedPreferences.getString("email", "")
                    if(position>=0) {
                        var status = values[position]
                        userMoviesViewModel.addUserMovies(UserMovies(email!!, movieId, status))
//                        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
            })
            .setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                }

            })

        return builder.create()
    }
}