package com.example.moviereview.db.local.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Accounts
import com.example.moviereview.db.local.entities.Users
import com.example.moviereview.db.local.repositories.AccountsRepository
import com.example.moviereview.db.local.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UsersViewModel (application: Application) : AndroidViewModel(application){
    var user : List<Users>? =null
    private val repository : UsersRepository

    lateinit var userByEmail : LiveData<List<Users>>
    init {
        val usersDao = MovieDatabase.getDatabase(application)?.usersDao()
        repository = UsersRepository(usersDao)

    }

    fun readUserByName(name:String) : List<Users>?
    {
        val job= viewModelScope.launch(Dispatchers.IO) {
            user = repository.readUserByName(name)
            Log.i("signup","1")
        }
        runBlocking {
            job.join()
        }
        Log.i("signup","2")
        return user
    }
    fun addUser(user: Users)
    {

        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: Users)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun getUserByEmail(email:String)
    {
        userByEmail = repository.getUserByEmail(email)
    }
}