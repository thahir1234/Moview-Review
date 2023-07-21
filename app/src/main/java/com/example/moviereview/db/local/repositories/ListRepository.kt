package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.ListsDao
import com.example.moviereview.db.local.entities.Lists
import com.example.moviereview.db.local.entities.TrendingMovies

class ListRepository (private val listsDao: ListsDao){
    var allLists = listsDao.getAllLists()

    lateinit var allListsByEmail : LiveData<List<Lists>>
    lateinit var allListsByListId : LiveData<List<Lists>>
    suspend fun addList(list: Lists) : Long
    {
        return listsDao.addList(list)
    }

    fun getListsByEmail(email:String) : LiveData<List<Lists>>
    {
        allListsByEmail = listsDao.getListsByEmail(email)
        return allListsByEmail
    }

    fun getListsByListId(listId : Int) : LiveData<List<Lists>>
    {
        allListsByListId = listsDao.getListsByListId(listId)
        return allListsByListId
    }
}
