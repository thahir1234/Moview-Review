package com.example.moviereview.db.local.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.UsersDao
import com.example.moviereview.db.local.entities.Users

class UsersRepository(private val usersDao: UsersDao?) {

    lateinit var  user : List<Users>

    lateinit var userByEmail : LiveData<List<Users>>

    suspend fun readUserByName(name:String) : List<Users>
    {
        user = usersDao?.readUserByName(name)!!
        return user
    }
    suspend fun addUser(user: Users)
    {
        usersDao?.addUser(user)
        Log.i("sqlCommands","Inserting a User record :"+user.email)

    }

    suspend fun updateUser(user: Users)
    {
        usersDao?.updateUser(user)
    }

    fun getUserByEmail(email:String) : LiveData<List<Users>>
    {
        userByEmail = usersDao?.readUserByEmail(email)!!
        return userByEmail
    }
}