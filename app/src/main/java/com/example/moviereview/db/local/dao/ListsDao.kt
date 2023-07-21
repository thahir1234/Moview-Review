package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.Lists

@Dao
interface ListsDao {

    @Upsert
    suspend fun addList(list:Lists) : Long

    @Query("select * from tblLists order by listId")
    fun getAllLists() : LiveData<List<Lists>>

    @Query("select * from tblLists where email = :email")
    fun getListsByEmail(email:String) : LiveData<List<Lists>>

    @Query("select * from tblLists where listId = :listId")
    fun getListsByListId(listId:Int) : LiveData<List<Lists>>

    //sorting

    @Query("select * from tblLists order by title asc")
    fun getListsByTitleAsc() : LiveData<List<Lists>>

    @Query("select * from tblLists order by title desc")
    fun getListsByTitleDesc() : LiveData<List<Lists>>

    @Query("select * from tblLists order by date(dateCreated) asc")
    fun getListsByDateAsc() : LiveData<List<Lists>>

    @Query("select * from tblLists order by date(dateCreated) desc")
    fun getListsByDateDesc() : LiveData<List<Lists>>
}