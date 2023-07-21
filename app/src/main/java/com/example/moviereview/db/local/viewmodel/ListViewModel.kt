package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.db.local.entities.TrendingMovies
import com.example.moviereview.db.local.repositories.ListRepository
import com.example.moviereview.db.local.repositories.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(application: Application) : AndroidViewModel(application) {
    var isAsc = true
    var isDsc = false
    var isName = true
    var isRating = false
    var isDate = false

    private val repository : ListRepository

    var allLists : LiveData<List<Lists>>
    var newListId : MutableLiveData<Long> = MutableLiveData()

    lateinit var allListsByEmail : LiveData<List<Lists>>
    lateinit var allListsByListId : LiveData<List<Lists>>

    lateinit var allListsTitleAsc : LiveData<List<Lists>>
    lateinit var allListsTitleDesc : LiveData<List<Lists>>
    lateinit var allListsDateAsc : LiveData<List<Lists>>
    lateinit var allListsDateDesc : LiveData<List<Lists>>

    init {
        val listsDao = MovieDatabase.getDatabase(application)?.listsDao()
        repository = ListRepository(listsDao!!)
        allLists = repository.allLists
        allListsTitleAsc = listsDao.getListsByTitleAsc()
        allListsTitleDesc = listsDao.getListsByTitleDesc()
        allListsDateAsc = listsDao.getListsByDateAsc()
        allListsDateDesc = listsDao.getListsByDateDesc()
    }

    fun addList(list: Lists)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addList(list)
            withContext(Dispatchers.Main)
            {
                newListId.value = result
            }
        }
    }


    fun getListsByEmail(email:String)
    {
        allListsByEmail =  repository.getListsByEmail(email)
    }

    fun getListsByListId(listId : Int)
    {
        allListsByListId = repository.getListsByListId(listId)
    }
}