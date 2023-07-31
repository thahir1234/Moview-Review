package com.example.moviereview.view.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivityDefaultBinding
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.fragments.FavouriteFragment
import com.example.moviereview.view.fragments.HomeFragment
import com.example.moviereview.view.fragments.SearchFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView


class DefaultActivity : AppCompatActivity() {

    lateinit var binding: ActivityDefaultBinding
    var homeFragment: HomeFragment = HomeFragment()
    var searchFragment: SearchFragment = SearchFragment()

    var favouriteFragment: FavouriteFragment = FavouriteFragment()

    lateinit var activeFragment: Fragment
    lateinit var usersViewModel: UsersViewModel

    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("default_activity","oncreate of default")
        super.onCreate(savedInstanceState)
        binding = com.example.moviereview.databinding.ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val email = sharedPreferences.getString("email","")


        activeFragment = homeFragment

//        supportFragmentManager.beginTransaction().add(R.id.fragment_fl,favouriteFragment,"Favourite").hide(favouriteFragment).commit()
//        supportFragmentManager.beginTransaction().add(R.id.fragment_fl,searchFragment,"Search").hide(searchFragment).commit()
//
//
//        supportFragmentManager.beginTransaction().add(R.id.fragment_fl,homeFragment,"Home").hide(homeFragment).commit()


        addFragment(homeFragment)
        binding.bottomNav1.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.home_page->
                {
                    addFragment(homeFragment)
                }
                R.id.search_page->
                {
                    addFragment(searchFragment)
                }
                R.id.fav_page->
                {
//                    sharedPreferences.edit().apply{
//                        putBoolean("loggedIn",false)
//                        apply()
//                    }
//                    startActivity(Intent(this,LoginActivity::class.java))
                    addFragment(favouriteFragment)
                }
            }
            true
        }

        binding.navView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.nav_drawer_logout->{
                        AlertDialog.Builder(this@DefaultActivity).apply {
                            setTitle("Do you wish to log out?")
                            setMessage("You will be redirected to the login screen")
                            setPositiveButton("Log out",object:DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    sharedPreferences.edit().clear().apply()
//                                    sharedPreferences.edit().apply{
//                                        putBoolean("loggedIn",false)
//                                        apply()
//                                    }
                                    startActivity(Intent(applicationContext,LoginActivity::class.java))
                                }
                            })
                            setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                }
                            })
                            show()
                        }
                    }
                    R.id.home_nav->
                    {
                        addFragment(homeFragment)
                    }
                    R.id.nav_drawer_yours->{
                        startActivityForResult(Intent(applicationContext,YoursActivity::class.java),100)
                        binding.navView.clearFocus()
                    }
                    R.id.nav_drawer_watching->{
                        val intent = Intent(applicationContext,WatchingActivity::class.java)
                        intent.putExtra("key","Watching")
                        startActivityForResult(intent,100)
                        binding.navView.clearFocus()

                    }
                    R.id.nav_drawer_watched->{
                        val intent = Intent(applicationContext,WatchingActivity::class.java)
                        intent.putExtra("key","Watched")
                        startActivityForResult(intent,100)
                        binding.navView.clearFocus()

                    }
                    R.id.nav_drawer_watchlist->{
                        val intent = Intent(applicationContext,WatchingActivity::class.java)
                        intent.putExtra("key","Watchlist")
                        startActivityForResult(intent,100)
                        binding.navView.clearFocus()
                    }
                    R.id.nav_drawer_profile->{
                        startActivityForResult(Intent(applicationContext,ProfileActivity::class.java),100)
                        binding.navView.clearFocus()

                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

        })

        usersViewModel.getUserByEmail(email = email!!)
        usersViewModel.userByEmail.observe(this) {
            binding.navView.findViewById<ShapeableImageView>(R.id.nav_profile_pic).setImageBitmap(it.get(0).image)
            binding.navView.findViewById<TextView>(R.id.nav_profile_name).text = it.get(0).name
            binding.navView.findViewById<TextView>(R.id.nav_profile_email).text = it.get(0).email
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("navDraw","outside result code")

        if(resultCode == 100)
        {
            Log.i("navDraw","inside result code")
            binding.navView.menu.findItem(R.id.home_nav).setChecked(true)
        }
    }
    private fun addFragment(frag : Fragment)
    {
        supportFragmentManager.beginTransaction().apply {
//            hide(activeFragment)
//            show(frag)
            replace(R.id.fragment_fl, frag)
            when(frag)
            {
                homeFragment->
                {
                    addToBackStack("Home")
                    binding.bottomNav1.menu.findItem(R.id.home_page).isChecked = true
                    binding.navView.menu.findItem(R.id.home_nav).setChecked(true)

                }
                searchFragment->
                {
                    addToBackStack("Search")
                    binding.bottomNav1.menu.findItem(R.id.search_page).setChecked(true)
                    binding.navView.menu.findItem(R.id.home_nav).setChecked(false)


                }
                favouriteFragment->
                {
                    addToBackStack("Favourite")
                    binding.bottomNav1.menu.findItem(R.id.fav_page).setChecked(true)
                    binding.navView.menu.findItem(R.id.home_nav).setChecked(false)
                }
            }
            commit()
        }
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            val lastFrag = getSupportFragmentManager().getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1);
            when(lastFrag.name)
            {
                "Home"->
                {

//                    val a = Intent(Intent.ACTION_MAIN)
//                    a.addCategory(Intent.CATEGORY_HOME)
//                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    Log.i("shared",sharedPreferences.getBoolean("loggedIn",false).toString())
//                    startActivity(a)
                    AlertDialog.Builder(this@DefaultActivity).apply {
                        setTitle("Do you want to exit?")
                        setMessage("You will be redirected out of the app")
                        setPositiveButton("exit",object:DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                val toast = Toast.makeText(applicationContext,"Exit",Toast.LENGTH_SHORT)
                                HelperFunction.showToast(toast,resources)
                               finishAffinity()
                            }
                        })
                        setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {

                            }
                        })
                        show()
                    }
                }
                else->
                {
//                    Toast.makeText(this,count.id.toString()+" "+R.id.home,Toast.LENGTH_SHORT).show()
                    addFragment(homeFragment)
                }
            }
//            Toast.makeText(this,count.name,Toast.LENGTH_SHORT).show()

        }
    }

}