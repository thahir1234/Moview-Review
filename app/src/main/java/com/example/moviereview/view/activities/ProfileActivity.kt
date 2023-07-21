package com.example.moviereview.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityProfileBinding
import com.example.moviereview.db.local.entities.Users
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction


class ProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityProfileBinding

    lateinit var sharedPreferences : SharedPreferences

    lateinit var usersViewModel: UsersViewModel

    private var email =""
    private var bio = ""
    private var name = ""
    var uri :Uri = Uri.EMPTY
    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<TextView>(R.id.actname_tv).text  = "Profile"
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.tick_iv).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }

        bitmap = ContextCompat.getDrawable(this, R.drawable.account_profile)?.toBitmap()!!
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        email = sharedPreferences.getString("email","").toString()

        usersViewModel.getUserByEmail(email)
        usersViewModel.userByEmail.observe(this)
        {
            if(savedInstanceState!=null)
            {
                binding.nameEtContainer.editText?.setText(savedInstanceState.getString("name"))
                binding.bioEtContainer.editText?.setText(savedInstanceState.getString("bio"))
            }
            else {
                binding.profileNameEt.setText(it.get(0).name)
                binding.profileDescEt.setText(it.get(0).bio)
            }
            bitmap = it.get(0).image
            binding.profilePicIv.setImageBitmap(it.get(0).image)
//            if(it.get(0).image == ContextCompat.getDrawable(this, R.drawable.account_profile)?.toBitmap()!!) {
//                binding.profilePicIv.setImageBitmap(ContextCompat.getDrawable(this, R.drawable.account_profile_black)?.toBitmap()!!)
//            }
        }

        binding.profilePicEditIv.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Title"),1)
        }

        findViewById<ImageView>(R.id.tick_iv).setOnClickListener {
            usersViewModel.addUser(Users(email = email, name = binding.profileNameEt.text.toString(), bio = binding.profileDescEt.text.toString(), image = bitmap, dateCreated = "", userId = kotlin.random.Random.nextInt(0,100)))
            val toast = Toast.makeText(this,"Updated!",Toast.LENGTH_SHORT)
            HelperFunction.showToast(toast,resources)
            onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i("profile","onsave")
        val name = binding.nameEtContainer.editText?.text.toString()
        val bio = binding.bioEtContainer.editText?.text.toString()

        Log.i("profile",name + " " + bio)

        outState.putString("name",name)
        outState.putString("bio",bio)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.i("profile","onrestore")



        Log.i("profile",savedInstanceState.getString("name") + " "+ savedInstanceState.getString("bio"))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && data!=null)
        {

            uri = data?.data!!
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            binding.profilePicIv.setImageBitmap(bitmap)
            //findViewById<ShapeableImageView>(R.id.your_review_profile).setImageBitmap(bitmap)

        }
    }

    override fun onStop() {
        super.onStop()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.VISIBLE

    }
}