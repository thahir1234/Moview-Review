package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.LoadedStatus
import com.example.moviereview.db.local.repositories.LoadedStatusRepository
import com.example.moviereview.db.local.repositories.ReviewsRepository
import com.example.moviereview.view.adapter.ListsRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadedStatusViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository : LoadedStatusRepository

    val allData : LiveData<List<LoadedStatus>>
    init {
        val loadedStatusDao = MovieDatabase.getDatabase(application)?.loadedStatusDao()
        repository = LoadedStatusRepository(loadedStatusDao!!)
        allData = repository.allData
    }

    fun addLoadedStatus(loadedStatus: LoadedStatus)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoadedStatus(loadedStatus)
        }
    }

    fun deleteAllData()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun updatePopular()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePopular()
        }
    }

    fun updateTrending()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTrending()
        }
    }
}