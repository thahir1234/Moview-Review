package com.example.moviereview.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.db.local.viewmodel.ListViewModel
import com.example.moviereview.view.activities.ListActivity
import java.text.SimpleDateFormat
import java.util.*

class AddListDialog : AppCompatDialogFragment() {
    lateinit var listViewModel: ListViewModel

    lateinit var sharedPreferences: SharedPreferences

    private var email=""
    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder:AlertDialog.Builder =AlertDialog.Builder(requireContext())
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        email = sharedPreferences.getString("email", "").toString()

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        val view = layoutInflater.inflate(R.layout.dialog_layout,null)

        val listName = view.findViewById<TextView>(R.id.create_list_name)
        val listDesc = view.findViewById<TextView>(R.id.create_list_desc)
        val visibilityPrivate = view.findViewById<RadioButton>(R.id.visibility_private)
        val visibilityPublic = view.findViewById<RadioButton>(R.id.visibility_public)
        builder.apply {
            setView(view)
            setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }

            })
            setPositiveButton("Create",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val date = Date()
                    val current = formatter.format(date)
                    val visibility:String = if(visibilityPrivate.isChecked)
                    {
                        "Private"
                    }
                    else
                    {
                        "Public"
                    }
                    listViewModel.addList(Lists(title = listName.text.toString(), email = email, visibility = visibility, dateCreated = current))
                    listViewModel.newListId.observe(requireActivity())
                    {
                        Log.i("listId",it.toString())
                        val intent = Intent(requireContext(),ListActivity::class.java)
                        intent.putExtra("listName",listName.text.toString())
                        intent.putExtra("listDesc",listDesc.text.toString())
                        intent.putExtra("listId",it)
                        intent.putExtra("listDate",current)
                        startActivity(intent)
                    }

                }

            })
        }


        return builder.create()
    }
}