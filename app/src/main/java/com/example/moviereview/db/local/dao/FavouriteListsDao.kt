package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.FavouriteLists
import com.example.moviereview.db.local.entities.FavouriteMovies
import com.example.moviereview.db.local.entities.Lists

@Dao
interface FavouriteListsDao {

    @Upsert
    suspend fun addFavList(list: FavouriteLists)

    @Query("select * from tblFavouriteLists where email = :email")
    fun getFavLists(email:String) : LiveData<List<FavouriteLists>>

    @Query("select * from tblFavouriteLists where listId = :listId")
    fun getFavLists2(listId: Int) : LiveData<List<FavouriteLists>>

    @Query("delete from tblFavouriteLists where email = :email and listId = :listId")
    suspend fun deleteFavList(email: String,listId:Int)
}