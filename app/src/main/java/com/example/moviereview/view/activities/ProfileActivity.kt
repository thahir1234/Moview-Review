package com.example.moviereview.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import java.io.ByteArrayOutputStream


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
        val imm: InputMethodManager = this
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        findViewById<TextView>(R.id.actname_tv).text  = "Profile"
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.tick_iv).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            val r=  Rect();
            //r will be populated with the coordinates of your view that area still visible.
            binding.root.getWindowVisibleDisplayFrame(r);

            val heightDiff =  binding.root.rootView.getHeight() - r.height();
            if (heightDiff > 0.25*binding.root.getRootView().getHeight()) { // if more than 25% of the screen, its probably a keyboard...
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            else {
                onBackPressed()
            }
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
            val r=  Rect();
            //r will be populated with the coordinates of your view that area still visible.
            binding.root.getWindowVisibleDisplayFrame(r);

            val heightDiff =  binding.root.rootView.getHeight() - r.height();
            if (heightDiff > 0.25*binding.root.getRootView().getHeight()) { // if more than 25% of the screen, its probably a keyboard...
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                val toast = Toast.makeText(this,"Updated!",Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,resources)
                onBackPressed()
            }
            else {
                usersViewModel.addUser(Users(email = email, name = binding.profileNameEt.text.toString(), bio = binding.profileDescEt.text.toString(), image = bitmap, dateCreated = "", userId = kotlin.random.Random.nextInt(0,100)))
                val toast = Toast.makeText(this,"Updated!",Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,resources)
                onBackPressed()
            }

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
//            bitmap = compressImage(getBytes(bitmap))
            binding.profilePicIv.setImageBitmap(bitmap)
            //findViewById<ShapeableImageView>(R.id.your_review_profile).setImageBitmap(bitmap)

        }
    }

    fun getBytes(bitmap: Bitmap) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream)
        return stream.toByteArray()
    }

    fun compressImage(imageToCompress : ByteArray) : Bitmap
    {
        var compressImage = imageToCompress
        if(compressImage.size>500000)
        {
            val bitmap = BitmapFactory.decodeByteArray(compressImage,0,compressImage.size)
            val resized = Bitmap.createScaledBitmap(bitmap,(bitmap.width*0.8).toInt(),(bitmap.height*0.8).toInt(),true)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,0,stream)
            compressImage = stream.toByteArray()
        }
        return BitmapFactory.decodeByteArray(compressImage, 0, compressImage.size);

    }
    override fun onStop() {
        super.onStop()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        findViewById<ImageView>(R.id.tick_iv).visibility = View.VISIBLE

    }
    override fun onBackPressed() {
        setResult(100, Intent())
        finish()
    }
}