package com.example.moviereview.db.local.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.AccountsDao
import com.example.moviereview.db.local.entities.Accounts

class AccountsRepository (private val accountsDao: AccountsDao?) {

    val accountsData : LiveData<List<Accounts>>? = accountsDao?.readAllAccounts()

    suspend fun addAccount(account: Accounts)
    {
        accountsDao?.addAccount(account)
        Log.i("sqlCommands","Inserting a account record :"+account.email)
    }

    suspend fun deleteAccount(account: Accounts)
    {
        accountsDao?.deleteAccount(account)
    }

    suspend fun readAccountByEmail(email:String) : List<Accounts>?
    {
        return accountsDao?.readAccountByEmail(email)
    }


}