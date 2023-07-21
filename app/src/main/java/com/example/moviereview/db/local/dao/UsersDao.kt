package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviereview.db.local.entities.Users

@Dao
interface UsersDao {

    @Upsert
    suspend fun addUser(user: Users)

    @Update
    suspend fun updateUser(user: Users)

    @Query("SELECT * FROM tblUsers WHERE name = :name")
    suspend fun readUserByName(name:String) : List<Users>

    @Query("select * from tblUsers where email = :email")
    fun readUserByEmail(email:String) : LiveData<List<Users>>
}