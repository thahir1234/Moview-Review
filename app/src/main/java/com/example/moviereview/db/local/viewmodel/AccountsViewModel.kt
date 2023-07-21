package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.dao.AccountsDao
import com.example.moviereview.db.local.entities.Accounts
import com.example.moviereview.db.local.repositories.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountsViewModel(application: Application) : AndroidViewModel(application) {
    var accountsByEmail : List<Accounts>? =null
    val accountsData : LiveData<List<Accounts>>?
    private val repository : AccountsRepository

    init {
        val accountsDao = MovieDatabase.getDatabase(application)?.accountsDao()
        repository = AccountsRepository(accountsDao)
        accountsData = repository.accountsData
    }

    fun addAccount(account: Accounts)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAccount(account)
        }
    }

    fun deleteAccount(account: Accounts)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAccount(account)
        }
    }

    fun readAccountByEmail(email:String) :List<Accounts>?
    {
        val job = viewModelScope.launch(Dispatchers.IO) {
            accountsByEmail =  repository.readAccountByEmail(email)
        }
        runBlocking {
            job.join()
        }
        return accountsByEmail
    }
}