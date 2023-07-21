package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.FavouriteListsDao
import com.example.moviereview.db.local.dao.FavouriteMoviesDao
import com.example.moviereview.db.local.entities.FavouriteLists
import com.example.moviereview.db.local.entities.FavouriteMovies

class FavouriteListsRepository(private val favouriteListsDao: FavouriteListsDao) {

    lateinit var favListsByEmail: LiveData<List<FavouriteLists>>
    lateinit var favListsById: LiveData<List<FavouriteLists>>

    suspend fun addFavList(favList: FavouriteLists)
    {
        favouriteListsDao.addFavList(favList)
    }


    fun getFavListsByEmail(email:String) : LiveData<List<FavouriteLists>>
    {
        favListsByEmail = favouriteListsDao.getFavLists(email)
        return favListsByEmail
    }

    fun getFavListsById(listId: Int) : LiveData<List<FavouriteLists>>
    {
        favListsById = favouriteListsDao.getFavLists2(listId)
        return favListsById
    }
    suspend fun deleteFavList(email: String,listId:Int)
    {
        favouriteListsDao.deleteFavList(email,listId)
    }
}