package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.LikedLists
import com.example.moviereview.db.local.repositories.LikedListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikedListViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var likedListsByEmail : LiveData<List<LikedLists>>
    lateinit var likedListsByListId : LiveData<List<LikedLists>>
    lateinit var likedListsByBoth : LiveData<List<LikedLists>>

    private val repository: LikedListRepository
    init {
        val likedListsDao = MovieDatabase.getDatabase(application)?.likedListsDao()
        repository = LikedListRepository(likedListsDao!!)
    }

    fun addLikedList(likedList : LikedLists)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLikedList(likedList)
        }
    }

    fun getLikedListsByEmail(email : String) {
        likedListsByEmail = repository.getLikedListsByEmail(email)
    }

    fun getLikedListsByListId(listId:Int)
    {
        likedListsByListId = repository.getLikedListsByListId(listId)
    }

    fun getLikedListsByBoth(email: String,listId: Int)
    {
        likedListsByBoth = repository.getLikedListsByBoth(email,listId)
    }

    fun deleteLikedList(email: String,listId: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLikedList(email,listId)
        }
    }

}