package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviereview.db.local.entities.Accounts

@Dao
interface AccountsDao {

    @Upsert
    suspend fun addAccount(account: Accounts)

    @Delete
    suspend fun deleteAccount(account:Accounts)

    @Query("SELECT * FROM tblAuthentication")
    fun readAllAccounts() : LiveData<List<Accounts>>

    @Query("SELECT * FROM tblAuthentication where email =:email")
    suspend fun readAccountByEmail(email:String) : List<Accounts>
}