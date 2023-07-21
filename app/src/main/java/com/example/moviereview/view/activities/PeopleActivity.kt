package com.example.moviereview.view.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityPeopleBinding
import com.example.moviereview.db.remote.api.APIImplementation
import com.example.moviereview.db.remote.model.PersonDesc
import com.example.moviereview.utils.HelperFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleActivity : AppCompatActivity() {

    lateinit var binding : ActivityPeopleBinding

    lateinit var person : LiveData<PersonDesc>
    private var castId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ImageView>(R.id.back_iv).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.plus_iv).visibility = View.GONE
        findViewById<ImageView>(R.id.sort_iv).visibility = View.GONE
        binding.peopleBioTv.setOnClickListener {
            if(binding.peopleBioTv.ellipsize == null)
            {
                binding.peopleBioTv.ellipsize = TextUtils.TruncateAt.END
                binding.peopleBioTv.maxLines = 4
            }
            else
            {
                binding.peopleBioTv.ellipsize = null
                binding.peopleBioTv.maxLines = 199
            }
        }
        castId = intent.getIntExtra("castId",-1)
        getPeopleDetails()
    }

    private fun getPeopleDetails()
    {
        person = APIImplementation.getPerson(castId)!!

        person.observe(this)
        {
            bindPeopleData(it)
        }
    }

    private fun bindPeopleData(personDesc: PersonDesc)
    {
        if(personDesc.profilePic?.isNotEmpty() == true) {
            HelperFunction.loadImageGlide(this,"https://image.tmdb.org/t/p/original/" + personDesc.profilePic,binding.peoplePicIv)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + personDesc.profilePic)
//                withContext(Dispatchers.Main) {
//                    binding.peoplePicIv.setImageBitmap(bitmap)
//                }
//            }
        }
        else
        {
            binding.peoplePicIv.setImageResource(R.drawable.poster_not)
        }
        if(personDesc.biography.isEmpty())
        {
            binding.peopleBioTv.text = "-- No bio available --"
        }
        else {
            binding.peopleBioTv.text = personDesc.biography
        }

        if(personDesc.department.isEmpty())
        {
            binding.peopleDepartmentTv.text = "-- No data --"
        }
        else {
            binding.peopleDepartmentTv.text = personDesc.department
        }

        if(personDesc.actingName.isEmpty())
        {
            binding.peopleAnameTv.text = "-- No data --"
            findViewById<TextView>(R.id.actname_tv).text = "-- No data --"
        }
        else {
            binding.peopleAnameTv.text = personDesc.actingName
            findViewById<TextView>(R.id.actname_tv).text = personDesc.actingName

        }
        if(personDesc.realNames.isEmpty())
        {
            binding.peopleRnameTv.text = "--No data--"
        }
        else {
            binding.peopleRnameTv.text = personDesc.realNames.get(0)
        }

        if(personDesc.birthday==null || personDesc.deathday==null)
        {
            binding.peoplePeriodTv.text = "-- No data --"
        }
        else
        {
            binding.peoplePeriodTv.text = "${personDesc.birthday} - ${personDesc.deathday}"
        }
    }

}