package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.LikedListsDao
import com.example.moviereview.db.local.entities.LikedLists
import com.example.moviereview.db.local.entities.LikedReviews

class LikedListRepository (private val likedListsDao: LikedListsDao){
    lateinit var likedListsByEmail : LiveData<List<LikedLists>>
    lateinit var likedListsByListId : LiveData<List<LikedLists>>
    lateinit var likedListsByBoth : LiveData<List<LikedLists>>

    suspend fun addLikedList(likedList: LikedLists)
    {
        likedListsDao.addLikedList(likedList)
    }

    fun getLikedListsByEmail(email : String) : LiveData<List<LikedLists>>
    {
        likedListsByEmail = likedListsDao.getLikedListsByEmail(email)
        return likedListsByEmail
    }

    fun getLikedListsByListId(listId:Int) : LiveData<List<LikedLists>>
    {
        likedListsByListId = likedListsDao.getLikedListsByListId(listId)
        return likedListsByListId
    }

    fun getLikedListsByBoth(email: String,listId: Int) : LiveData<List<LikedLists>>
    {
        likedListsByBoth = likedListsDao.getLikedListsByBoth(email,listId)
        return likedListsByBoth
    }

    suspend fun deleteLikedList(email: String,listId: Int)
    {
        likedListsDao.deleteLikedList(email,listId)
    }
}
